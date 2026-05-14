package ru.cherryngine.engine.physics

import com.github.stephengold.joltjni.*
import com.github.stephengold.joltjni.enumerate.*
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.physics.terrain.ActiveBodyInfo
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
    private val vehicleByPhysicsId = HashMap<UUID, VehicleBody>()
    private val seenThisTick = HashSet<UUID>()

    /** Body-va хитбоксов игроков. Используется [ContextContactListener] для
     *  отбраковки пар player-hitbox ↔ terrain (клиент сам коллайдит с блоками,
     *  а server-hitbox не должен дублировать → откидывает камеру назад). */
    private val playerBodyVas: MutableSet<Long> = java.util.concurrent.ConcurrentHashMap.newKeySet()

    /** Body-va terrain'а (статические блоки). Парная сторона для отбраковки. */
    private val terrainBodyVas: MutableSet<Long> = java.util.concurrent.ConcurrentHashMap.newKeySet()

    fun getBodyBottomPosition(physicsId: UUID): Vec3D? {
        val body = bodyByPhysicsId[physicsId] ?: return null
        val center = body.getTransform().translation
        val bounds = body.getWorldBounds()
        return Vec3D(center.x, bounds.min.y, center.z)
    }

    fun getBodyTransform(physicsId: UUID): Transform? =
        bodyByPhysicsId[physicsId]?.getTransform()
            ?: vehicleByPhysicsId[physicsId]?.getTransform()

    fun getVehicleBody(physicsId: UUID): VehicleBody? = vehicleByPhysicsId[physicsId]

    /** Унифицированный сеттер velocity для тела ИЛИ машины по physicsId. */
    fun setLinearVelocity(physicsId: UUID, velocity: Vec3D) {
        bodyByPhysicsId[physicsId]?.setLinearVelocity(velocity)
            ?: vehicleByPhysicsId[physicsId]?.setLinearVelocity(velocity)
    }

    fun setAngularVelocity(physicsId: UUID, velocity: Vec3D) {
        bodyByPhysicsId[physicsId]?.setAngularVelocity(velocity)
            ?: vehicleByPhysicsId[physicsId]?.setAngularVelocity(velocity)
    }

    fun getOrCreateVehicleBody(physicsId: UUID, physContextIDs: Set<String>, factory: () -> VehicleBody): VehicleBody {
        return vehicleByPhysicsId.getOrPut(physicsId) {
            factory().also { vehicle ->
                if (physContextIDs.isNotEmpty()) bodyContexts[vehicle.body.va()] = physContextIDs
            }
        }
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
        // Ищем только MOVING тела (кубы и другие хитбоксы игроков) — НЕ terrain.
        // Шалкер-платформа смысл имеет только для "искусственных" поверхностей поверх terrain.
        physicsSystem.narrowPhaseQuery.castShape(
            shapeCast, ShapeCastSettings(), shiftedStart, collector,
            BroadPhaseLayerFilter(), SpecifiedObjectLayerFilter(Layers.MOVING), ignoreFilter
        )
        if (!collector.hadHit()) return null
        val fraction = collector.hit.fraction.toDouble()
        return startShapeBottomY - fraction * totalDist
    }

    /**
     * Swept shape-cast формой тела [physicsId] из точки [from] на вектор [offset].
     * Возвращает fraction хита [0.0..1.0] (какую долю пути тело прошло до столкновения),
     * либо `null` если путь свободен. Само тело исключается из cast'а.
     * Используется для детекции "впереди препятствие" в step-up логике хитбокса.
     */
    fun castShapeFrom(physicsId: UUID, from: Vec3D, offset: Vec3D): Double? {
        val physicsBody = bodyByPhysicsId[physicsId] ?: return null
        val body = physicsBody.body
        val shape = body.shape
        val startR = RVec3(from.x, from.y, from.z)
        val shapeCast = RShapeCast(shape, Vec3(1f, 1f, 1f), RMat44.sTranslation(startR), offset.joltVec3())
        val collector = ClosestHitCastShapeCollector()
        val ignoreFilter = IgnoreMultipleBodiesFilter().apply { ignoreBody(body.getId()) }
        physicsSystem.narrowPhaseQuery.castShape(
            shapeCast, ShapeCastSettings(), startR, collector,
            BroadPhaseLayerFilter(), ObjectLayerFilter(), ignoreFilter
        )
        if (!collector.hadHit()) return null
        return collector.hit.fraction.toDouble()
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

    /**
     * Собирает AABB + velocity + контексты всех живых тел в этом тике.
     * Используется для [TerrainGenerator] и других consumer'ов, которым нужен
     * снимок активных тел.
     */
    fun collectActiveBodies(): List<ActiveBodyInfo> {
        val result = ArrayList<ActiveBodyInfo>(bodyByPhysicsId.size + vehicleByPhysicsId.size)
        for (body in bodyByPhysicsId.values) {
            result += ActiveBodyInfo(
                aabb = body.getWorldBounds(),
                velocity = body.getLinearVelocity(),
                physContextIDs = bodyContexts[body.body.va()] ?: emptySet(),
            )
        }
        // Машины тоже должны попадать в TerrainGenerator — иначе вокруг них не
        // создаётся коллизионный terrain и колёса проваливаются в пустоту.
        for (vehicle in vehicleByPhysicsId.values) {
            result += ActiveBodyInfo(
                aabb = vehicle.getWorldBounds(),
                velocity = vehicle.getLinearVelocity(),
                physContextIDs = bodyContexts[vehicle.body.va()] ?: emptySet(),
            )
        }
        return result
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
            return createBody(bodySettings, EActivation.DontActivate).also {
                terrainBodyVas.add(it.body.va())
            }
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
        return createBody(bodySettings, EActivation.DontActivate).also {
            terrainBodyVas.add(it.body.va())
        }
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
        return createBody(bodySettings, EActivation.Activate).also { body ->
            playerBodyVas.add(body.body.va())
        }
    }

    /**
     * Создаёт колёсный автомобиль через Jolt VehicleConstraint:
     * шасси-box + 4 колеса, передние с рулением, задние ведущие.
     * Колёса как отдельные тела не создаются — Jolt управляет их транформами
     * через VehicleConstraint. Внешним консьюмерам — только чтение через
     * [VehicleBody.getWheelTransform].
     */
    fun addCar(position: Vec3D, settings: CarSettings): VehicleBody {
        // RWD drift-spec купе. Все настраиваемые параметры — в [CarSettings];
        // здесь только pure assembly через Jolt-API.
        val halfWidth = settings.chassisSize.x.toFloat() * 0.5f
        val halfHeight = settings.chassisSize.y.toFloat() * 0.5f
        val halfWheelbase = settings.wheelbase * 0.5f
        val attachY = 0f
        val maxSteer = Math.toRadians(settings.maxSteerAngleDegrees.toDouble()).toFloat()

        // Шасси с OffsetCenterOfMassShape — центр масс опускается в дно chassis.
        // Без этого высокая машина легко переворачивается на резком манёвре.
        // linear/angularDamping не задаём — defaults (0.05/0.05) как у sample'а.
        val halfExtents = settings.chassisSize.joltVec3().apply { scaleInPlace(0.5f) }
        val boxShape = BoxShape(halfExtents)
        val comOffset = Vec3(0f, -halfHeight, 0f)
        val chassisShape = OffsetCenterOfMassShapeSettings(comOffset, boxShape).create().get()
        val chassisSettings = BodyCreationSettings()
            .setMotionType(EMotionType.Dynamic)
            .setObjectLayer(Layers.MOVING)
            .setShape(chassisShape)
            .setPosition(position.joltRVec3())
            .setMassPropertiesOverride(MassProperties().apply { setMass(settings.chassisMass) })
            .setOverrideMassProperties(EOverrideMassProperties.CalculateInertia)

        // Lateral friction curve по slip-angle (degrees → coefficient).
        // (0°, 0) → (3°, peak) → (20°, peak*0.85) → (90°, peak*0.7) — стандартная
        // tire-curve: пик на малых углах, спад при высоком скольжении. sort()
        // обязателен — Jolt LinearCurve не сортирует автоматически после addPoint.
        fun lateralFrictionCurve(peak: Float): LinearCurve = LinearCurve().apply {
            addPoint(0f, 0f)
            addPoint(3f, peak)
            addPoint(20f, peak * 0.85f)
            addPoint(90f, peak * 0.7f)
            sort()
        }

        // Longitudinal friction curve по slip-ratio (0..1 → coefficient).
        val longitudinalFriction = LinearCurve().apply {
            addPoint(0f, 0f)
            addPoint(0.06f, settings.longitudinalFrictionPeak)
            addPoint(0.2f, settings.longitudinalFrictionPeak * 0.9f)
            sort()
        }
        val frontLateralFriction = lateralFrictionCurve(settings.frontLateralFrictionPeak)
        val rearLateralFriction = lateralFrictionCurve(settings.rearLateralFrictionPeak)

        // 4 колеса в углах: 0=FL, 1=FR, 2=RL, 3=RR. Передним handBrake отключаем
        // (sample-конвенция), задним — settings.rearHandBrakeTorque.
        fun makeWheel(localX: Float, localZ: Float, steerable: Boolean): WheelSettingsWv {
            val wheel = WheelSettingsWv()
            wheel.setPosition(Vec3(localX, attachY, localZ))
            wheel.setRadius(settings.wheelRadius)
            wheel.setWidth(settings.wheelWidth)
            wheel.setSuspensionMinLength(settings.suspensionMinLength)
            wheel.setSuspensionMaxLength(settings.suspensionMaxLength)
            wheel.setSuspensionDirection(Vec3(0f, -1f, 0f))
            wheel.setSteeringAxis(Vec3(0f, 1f, 0f))
            wheel.setWheelForward(Vec3(0f, 0f, 1f))
            wheel.setWheelUp(Vec3(0f, 1f, 0f))
            wheel.setMaxSteerAngle(if (steerable) maxSteer else 0f)
            wheel.setMaxHandBrakeTorque(if (steerable) 0f else settings.rearHandBrakeTorque)
            wheel.suspensionSpring.setFrequency(settings.suspensionFrequency)
            wheel.suspensionSpring.setDamping(settings.suspensionDamping)
            wheel.setLongitudinalFriction(longitudinalFriction)
            wheel.setLateralFriction(if (steerable) frontLateralFriction else rearLateralFriction)
            return wheel
        }

        val frontLeft  = makeWheel( halfWidth,  halfWheelbase, steerable = true)
        val frontRight = makeWheel(-halfWidth,  halfWheelbase, steerable = true)
        val rearLeft   = makeWheel( halfWidth, -halfWheelbase, steerable = false)
        val rearRight  = makeWheel(-halfWidth, -halfWheelbase, steerable = false)

        // RWD: один differential на задние колёса (2=RL, 3=RR).
        val controllerSettings = WheeledVehicleControllerSettings()
        controllerSettings.engine.setMaxTorque(settings.engineMaxTorque)
        controllerSettings.engine.setMaxRpm(settings.engineMaxRpm)
        controllerSettings.setNumDifferentials(1)
        val rearDiff = controllerSettings.getDifferential(0)
        rearDiff.setLeftWheel(2)
        rearDiff.setRightWheel(3)
        rearDiff.setEngineTorqueRatio(1f)
        rearDiff.setLimitedSlipRatio(settings.limitedSlipRatio)

        val constraintSettings = VehicleConstraintSettings()
        constraintSettings.setForward(Vec3(0f, 0f, 1f))
        constraintSettings.setUp(Vec3(0f, 1f, 0f))
        constraintSettings.setMaxPitchRollAngle(
            Math.toRadians(settings.maxPitchRollAngleDegrees.toDouble()).toFloat()
        )
        // Anti-roll bars asymmetric: передний жёсткий — стабильная реакция руля,
        // задний помягче — даёт корме скользить в дрифте.
        constraintSettings.setNumAntiRollBars(2)
        constraintSettings.getAntiRollBar(0)
            .setLeftWheel(0).setRightWheel(1).setStiffness(settings.antiRollBarFrontStiffness)
        constraintSettings.getAntiRollBar(1)
            .setLeftWheel(2).setRightWheel(3).setStiffness(settings.antiRollBarRearStiffness)
        constraintSettings.addWheels(frontLeft, frontRight, rearLeft, rearRight)
        constraintSettings.setController(controllerSettings)

        return VehicleBody(chassisSettings, constraintSettings).also { vehicle ->
            vehicle.controller.setDifferentialLimitedSlipRatio(settings.limitedSlipRatio)
            // setTireMaxImpulseCallback (longitudinal × 10) — sample-хак, нужен был
            // в старых версиях Jolt (баг в impulse-баланса). В Jolt 4.3+ исправлено,
            // overkill multiplier ломает lateral grip → машина едет как по льду.
            // Оставляем default-балансировку Jolt.
        }
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
        for (vehicle in vehicleByPhysicsId.values) {
            val speed = vehicle.body.getLinearVelocity().length()
            val quality = if (speed > linearCastSpeedThreshold) EMotionQuality.LinearCast else EMotionQuality.Discrete
            bodyInterface.setMotionQuality(vehicle.body.id, quality)
        }

        val steps = 1
        physicsSystem.update(delta, steps, tempAllocator, jobSystem).also { errors ->
            check(errors == EPhysicsUpdateError.None) { errors }
        }

        // Unseen-cleanup: тела/машины, которым не звали keepAlive в этом тике, удаляются
        val toRemove = bodyByPhysicsId.keys.filter { it !in seenThisTick }
        toRemove.forEach { uuid ->
            val body = bodyByPhysicsId.remove(uuid)!!
            unregisterBodyContexts(body)
            body.remove()
        }
        val toRemoveVehicles = vehicleByPhysicsId.keys.filter { it !in seenThisTick }
        toRemoveVehicles.forEach { uuid ->
            val vehicle = vehicleByPhysicsId.remove(uuid)!!
            bodyContexts.remove(vehicle.body.va())
            vehicle.remove()
        }
        seenThisTick.clear()
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
            val va = body.va()
            physicsSystem.getBodyInterface().removeBody(body.id)
            physicsSystem.getBodyInterface().destroyBody(body.id)
            bodyContexts.remove(va)
            playerBodyVas.remove(va)
            terrainBodyVas.remove(va)
        }
    }

    /**
     * Колёсный автомобиль: chassis Body + VehicleConstraint + WheeledVehicleController.
     * Колёса — собственность constraint'а, отдельных тел не имеют. Чтение трансформов
     * через [getWheelTransform]; запись driver-input через [setDriverInput].
     */
    inner class VehicleBody(
        chassisSettings: BodyCreationSettings,
        constraintSettings: VehicleConstraintSettings,
    ) {
        val body: Body
        val constraint: VehicleConstraint
        val controller: WheeledVehicleController
        private val stepListener: VehicleStepListener
        private val collisionTester: VehicleCollisionTesterCastCylinder

        init {
            val bodyInterface = physicsSystem.getBodyInterface()
            body = bodyInterface.createBody(chassisSettings)
            bodyInterface.addBody(body, EActivation.Activate)

            constraint = VehicleConstraint(body, constraintSettings)
            // CastCylinder использует реальную геометрию колеса (radius+width из WheelSettings).
            // Layer аргумент = "слой который представляет колесо как тестируемый объект"; через
            // ObjectLayerPairFilter из этого выводится какие слои тестировать. У нас MOVING↔NON_MOVING
            // включена, поэтому передача MOVING заставит фильтр находить NON_MOVING-тела (terrain).
            // С NON_MOVING фильтр бы исключил terrain (NON_MOVING↔NON_MOVING выключена) и колёса
            // искали бы только MOVING-тела (другие машины/кубы) — отсюда «проваливаются».
            collisionTester = VehicleCollisionTesterCastCylinder(Layers.MOVING, 0f)
            constraint.setVehicleCollisionTester(collisionTester)
            physicsSystem.addConstraint(constraint)
            stepListener = constraint.getStepListener()
            physicsSystem.addStepListener(stepListener)

            controller = constraint.getController() as WheeledVehicleController
        }

        fun getTransform(): Transform {
            val rVec3 = RVec3()
            val quat = Quat()
            body.getPositionAndRotation(rVec3, quat)
            return Transform(rVec3.vec3D(), quat.qRot())
        }

        fun setDriverInput(forward: Float, right: Float, brake: Float, handBrake: Float) {
            controller.setDriverInput(forward, right, brake, handBrake)
        }

        /** Активирует chassis (вытягивает из sleep), чтобы driver-input применился. */
        fun activate() {
            physicsSystem.getBodyInterface().activateBody(body.id)
        }

        fun getLinearVelocity(): Vec3D = body.getLinearVelocity().vec3D()

        fun setLinearVelocity(velocity: Vec3D) {
            physicsSystem.getBodyInterface().setLinearVelocity(body.id, velocity.joltVec3())
        }

        fun setAngularVelocity(velocity: Vec3D) {
            physicsSystem.getBodyInterface().setAngularVelocity(body.id, velocity.joltVec3())
        }

        fun getWorldBounds(): Cuboid = body.getWorldSpaceBounds().cuboid()

        /** World-transform колеса по индексу. wheelRight/wheelUp в локальном фрейме шасси (стандартные). */
        fun getWheelTransform(wheelIndex: Int): Transform {
            val mat = constraint.getWheelWorldTransform(wheelIndex, Vec3(0f, 1f, 0f), Vec3(0f, 0f, 1f))
            val translation = mat.getTranslation()
            val rotation = mat.getQuaternion()
            return Transform(translation.vec3D(), rotation.qRot())
        }

        fun remove() {
            physicsSystem.removeStepListener(stepListener)
            physicsSystem.removeConstraint(constraint)
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
            // Player-hitbox ↔ terrain: клиент сам с terrain коллайдит, server-hitbox
            // не должен дублировать — иначе hitbox задевает блок раньше клиента,
            // diff > STUCK_THRESHOLD, correctClientPosition тянет камеру назад
            // («откидывает» при ходьбе/полёте). Reject пару на этапе validate.
            val pairIsPlayerAndTerrain =
                (body1Va in playerBodyVas && body2Va in terrainBodyVas) ||
                (body2Va in playerBodyVas && body1Va in terrainBodyVas)
            if (pairIsPlayerAndTerrain) {
                return ValidateResult.RejectAllContactsForThisBodyPair.ordinal
            }

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
