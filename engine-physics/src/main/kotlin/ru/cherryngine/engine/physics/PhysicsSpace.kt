package ru.cherryngine.engine.physics

import com.github.stephengold.joltjni.*
import com.github.stephengold.joltjni.enumerate.*
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import java.util.*


@InstanceSingleton
class PhysicsSpace {
    val linearCastSpeedThreshold: Float = 10f
    val physicsSystem: PhysicsSystem
    val tempAllocator: TempAllocator
    val jobSystem: JobSystem

    val bodyContexts = HashMap<Long, Set<String>>()

    private val bodyByPhysicsId = HashMap<UUID, PhysicsBody>()
    private val seenThisTick = HashSet<UUID>()

    fun getBodyBottomPosition(physicsId: UUID): Vec3D? {
        val body = bodyByPhysicsId[physicsId] ?: return null
        val center = body.getTransform().translation
        val bounds = body.getWorldBounds()
        return Vec3D(center.x, bounds.min.y, center.z)
    }

    /**
     * Кастит форму хитбокса игрока вниз и возвращает Y верхней плоскости пола,
     * если она в пределах [maxDistance] блоков ниже ног. Иначе — null.
     * Старт луча — на 1 блок выше ног игрока: это даёт запас, если игрок уже
     * частично внутри блока, и shape-cast начинает выше проблемной зоны.
     * Terrain (static NON_MOVING) исключается — с ним игрок и так коллайдится.
     */
    fun castFloorBelow(physicsId: UUID, maxDistance: Double): Double? {
        val physicsBody = bodyByPhysicsId[physicsId] ?: return null
        val body = physicsBody.body
        val shape = body.shape
        val centerOfMass = body.centerOfMassPosition
        val bounds = physicsBody.getWorldBounds()
        val halfHeight = (bounds.max.y - bounds.min.y) * 0.5
        val feetY = centerOfMass.yy() - halfHeight
        val startShapeBottomY = feetY + 1.0
        val startCenterY = startShapeBottomY + halfHeight
        val shiftedStart = RVec3(centerOfMass.xx(), startCenterY, centerOfMass.zz())
        val comStart = RMat44.sTranslation(shiftedStart)
        val totalDist = 1.0 + maxDistance
        val offset = Vec3(0f, -totalDist.toFloat(), 0f)
        val shapeCast = RShapeCast(shape, Vec3(1f, 1f, 1f), comStart, offset)
        val collector = ClosestHitCastShapeCollector()
        val ignoreFilter = IgnoreMultipleBodiesFilter().apply { ignoreBody(body.getId()) }
        physicsSystem.narrowPhaseQuery.castShape(
            shapeCast, ShapeCastSettings(), shiftedStart, collector,
            BroadPhaseLayerFilter(), SpecifiedObjectLayerFilter(Layers.MOVING), ignoreFilter
        )
        if (!collector.hadHit()) return null
        val fraction = collector.hit.fraction.toDouble()
        return startShapeBottomY - fraction * totalDist
    }

    init {
        JoltLoader.checkAndInit()

        // For simplicity, use a single broadphase layer:
        val numBpLayers = 1
        val numObjLayers = 2

        val ovoFilter = ObjectLayerPairFilterTable(numObjLayers)
        // Enable collisions between 2 moving bodies:
        ovoFilter.enableCollision(Layers.MOVING, Layers.MOVING)
        // Enable collisions between a moving body and a non-moving one:
        ovoFilter.enableCollision(Layers.MOVING, Layers.NON_MOVING)
        // Disable collisions between 2 non-moving bodies:
        ovoFilter.disableCollision(Layers.NON_MOVING, Layers.NON_MOVING)


        // Map both object layers to broadphase layer 0:
        val layerMap = BroadPhaseLayerInterfaceTable(numObjLayers, numBpLayers)
        layerMap.mapObjectToBroadPhaseLayer(Layers.MOVING, 0)
        layerMap.mapObjectToBroadPhaseLayer(Layers.NON_MOVING, 0)


        // Rules for colliding object layers with broadphase layers:
        val ovbFilter: ObjectVsBroadPhaseLayerFilter =
            ObjectVsBroadPhaseLayerFilterTable(layerMap, numBpLayers, ovoFilter, numObjLayers)

        physicsSystem = PhysicsSystem()


        // Set high limits, even though this sample app uses only 2 bodies:
        val maxBodies = 5000
        val numBodyMutexes = 0 // 0 means "use the default number"
        val maxBodyPairs = 65536
        val maxContacts = 20480
        physicsSystem.init(maxBodies, numBodyMutexes, maxBodyPairs, maxContacts, layerMap, ovbFilter, ovoFilter)
        physicsSystem.optimizeBroadPhase()

        tempAllocator = TempAllocatorMalloc()
        val numWorkerThreads = Runtime.getRuntime().availableProcessors()
        jobSystem = JobSystemThreadPool(
            Jolt.cMaxPhysicsJobs,
            Jolt.cMaxPhysicsBarriers,
            numWorkerThreads
        ) // use all available processors

        // Default: -9.81f
        // Minecraft: -31.36f
        physicsSystem.setGravity(Vec3(0f, -17f, 0f))

        physicsSystem.setContactListener(ContextContactListener())
    }

    fun getOrCreateBody(physicsId: UUID, physContextIDs: Set<String>, factory: () -> PhysicsBody): PhysicsBody {
        return bodyByPhysicsId.getOrPut(physicsId) {
            factory().also { body ->
                if (physContextIDs.isNotEmpty()) registerBodyContexts(body, physContextIDs)
            }
        }
    }

    fun keepAlive(physicsId: UUID) {
        seenThisTick.add(physicsId)
    }

    fun beginTick() {
        seenThisTick.clear()
    }

    fun endTick() {
        val toRemove = bodyByPhysicsId.keys.filter { it !in seenThisTick }
        toRemove.forEach { uuid ->
            val body = bodyByPhysicsId.remove(uuid)!!
            unregisterBodyContexts(body)
            body.remove()
        }
    }

    fun addTerrain(pos: Vec3I, collisionCuboids: List<Cuboid>): PhysicsBody {
        if (collisionCuboids.size == 1) {
            val cuboid = collisionCuboids[0]
            val halfExtents = (cuboid.size * 0.5).joltVec3()
            val bodySettings = BodyCreationSettings()
                .setMotionType(EMotionType.Static)
                .setObjectLayer(Layers.NON_MOVING)
                .setShape(BoxShape(halfExtents))
                .setPosition(RVec3(pos.x + cuboid.centerX, pos.y + cuboid.centerY, pos.z + cuboid.centerZ))
            return createBody(bodySettings, EActivation.DontActivate)
        }

        val compoundSettings = StaticCompoundShapeSettings()
        for (cuboid in collisionCuboids) {
            val halfExtents = (cuboid.size * 0.5).joltVec3()
            val offset = (cuboid.center - Vec3D(0.5, 0.5, 0.5)).joltVec3()
            compoundSettings.addShape(offset, Quat.sIdentity(), BoxShape(halfExtents))
        }
        val bodySettings = BodyCreationSettings()
            .setMotionType(EMotionType.Static)
            .setObjectLayer(Layers.NON_MOVING)
            .setShape(compoundSettings.create().get())
            .setPosition(RVec3(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5))
        return createBody(bodySettings, EActivation.DontActivate)
    }

    fun addPlayer(position: Vec3D): PhysicsBody {
        val translationOnly =
            EAllowedDofs.TranslationX or EAllowedDofs.TranslationY or EAllowedDofs.TranslationZ
        val bodySettings = BodyCreationSettings()
            .setMotionType(EMotionType.Dynamic)
            .setObjectLayer(Layers.MOVING)
            .setShape(BoxShape(Vec3(0.3f, 0.9f, 0.3f)))
            .setAllowedDofs(translationOnly)
            .setGravityFactor(0f)
            .setLinearDamping(0f)
            .setMassPropertiesOverride(
                MassProperties().apply { setMass(100f) }
            )
            .setOverrideMassProperties(EOverrideMassProperties.CalculateInertia)
            .setPosition(position.joltRVec3())
        return createBody(bodySettings, EActivation.Activate)
    }

    fun addCube(position: Vec3D, size: Vec3D): PhysicsBody {
        val bodyCreationSettings = BodyCreationSettings()
            .setMotionType(EMotionType.Dynamic)
            .setObjectLayer(Layers.MOVING)
            .setShape(BoxShape(size.joltVec3().apply { scaleInPlace(0.5f) }))
            .setAngularDamping(0.1f)
            .setLinearDamping(0.3f)
            .setPosition(position.joltRVec3())
        return createBody(bodyCreationSettings, EActivation.Activate)
    }

    fun update(delta: Float) {
        // Динамически переключаем MotionQuality по скорости
        val bodyInterface = physicsSystem.getBodyInterface()
        for (body in bodyByPhysicsId.values) {
            if (body.body.isStatic) continue
            val speed = body.body.getLinearVelocity().length()
            val quality = if (speed > linearCastSpeedThreshold) EMotionQuality.LinearCast else EMotionQuality.Discrete
            bodyInterface.setMotionQuality(body.body.id, quality)
        }

        val steps = 1
        physicsSystem.update(delta, steps, tempAllocator, jobSystem).also { errors ->
            check(errors == EPhysicsUpdateError.None) { errors }
        }
    }

    fun createBody(bodyCreationSettings: BodyCreationSettings, eActivation: EActivation): PhysicsBody {
        return PhysicsBody(bodyCreationSettings, eActivation)
    }

    fun registerBodyContexts(body: PhysicsBody, contexts: Set<String>) {
        bodyContexts[body.body.va()] = contexts
    }

    fun updateBodyContexts(body: PhysicsBody, contexts: Set<String>) {
        bodyContexts[body.body.va()] = contexts
    }

    fun unregisterBodyContexts(body: PhysicsBody) {
        bodyContexts.remove(body.body.va())
    }

    fun destroy() {
        // TODO
    }

    inner class PhysicsBody(
        bodySettings: BodyCreationSettings,
        eActivation: EActivation,
    ) {
        val body: Body

        init {
            val bodyInterface = physicsSystem.getBodyInterface()
            this.body = bodyInterface.createBody(bodySettings)
            bodyInterface.addBody(body, eActivation)
        }

        fun getTransform(): Transform {
            val rVec3 = RVec3()
            val quat = Quat()
            body.getPositionAndRotation(rVec3, quat)
            return Transform(rVec3.vec3D(), quat.qRot())
        }

        fun teleport(position: Vec3D) {
            physicsSystem.getBodyInterface().setPosition(
                body.id,
                position.joltRVec3(),
                EActivation.Activate
            )
        }

        fun moveKinematic(position: Vec3D, delta: Float) {
            physicsSystem.getBodyInterface().moveKinematic(
                body.id,
                position.joltRVec3(),
                Quat.sIdentity(),
                delta
            )
        }

        fun setLinearVelocity(velocity: Vec3D) {
            physicsSystem.getBodyInterface().setLinearVelocity(body.id, velocity.joltVec3())
        }

        fun setAngularVelocity(velocity: Vec3D) {
            physicsSystem.getBodyInterface().setAngularVelocity(body.id, velocity.joltVec3())
        }

        fun getLinearVelocity(): Vec3D {
            return body.getLinearVelocity().vec3D()
        }

        fun getWorldBounds(): Cuboid = body.getWorldSpaceBounds().cuboid()

        fun remove() {
            physicsSystem.getBodyInterface().removeBody(body.id)
            physicsSystem.getBodyInterface().destroyBody(body.id)
            bodyContexts.remove(body.va())
        }
    }

    private inner class ContextContactListener : CustomContactListener() {
        override fun onContactValidate(
            body1Va: Long,
            body2Va: Long,
            baseOffsetX: Double,
            baseOffsetY: Double,
            baseOffsetZ: Double,
            manifoldVa: Long,
        ): Int {
            val ctx1 = bodyContexts[body1Va]
            val ctx2 = bodyContexts[body2Va]
            // Оба тела зарегистрированы (динамические) — проверяем пересечение контекстов
            if (ctx1 != null && ctx2 != null) {
                if (ctx1.none { it in ctx2 }) {
                    return ValidateResult.RejectAllContactsForThisBodyPair.ordinal
                }
            }
            // Terrain тела (не в bodyContexts) — всегда проходят,
            // их фильтрация уже сделана на уровне TerrainGenerator
            return ValidateResult.AcceptAllContactsForThisBodyPair.ordinal
        }
    }
}
