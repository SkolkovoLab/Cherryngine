package ru.cherryngine.engine.physics

import com.github.stephengold.joltjni.*
import com.github.stephengold.joltjni.enumerate.EActivation
import com.github.stephengold.joltjni.enumerate.EMotionType
import com.github.stephengold.joltjni.enumerate.EPhysicsUpdateError
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.rotation.QRot

class MinecraftPhysics {
    val physicsSystem: PhysicsSystem
    val tempAllocator: TempAllocator
    val jobSystem: JobSystem

    init {
        NativeLoader.checkAndInit()
        // https://github.com/stephengold/jolt-jni-docs/blob/master/java-apps/src/main/java/com/github/stephengold/sportjolt/javaapp/sample/console/HelloJoltJni.java
        //Jolt.setTraceAllocations(true); // to log Jolt-JNI heap allocations
        JoltPhysicsObject.startCleaner() // to reclaim native memory
        Jolt.registerDefaultAllocator() // tell Jolt Physics to use malloc/free
        Jolt.installDefaultAssertCallback()
        Jolt.installDefaultTraceCallback()
        Jolt.newFactory().also { success -> check(success) }
        Jolt.registerTypes()


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
    }

    fun update(delta: Float) {
        val steps = 1
        physicsSystem.update(delta, steps, tempAllocator, jobSystem).also { errors ->
            check(errors == EPhysicsUpdateError.None) { errors }
        }
    }

    val bodies = HashMap<Long, Aboba>()

    fun addCube(position: Vec3D, size: Vec3D): Aboba {
        val positionRVec3 = RVec3(position.x, position.y, position.z)
        val bodyCreationSettings = BodyCreationSettings()
            .setMotionType(EMotionType.Dynamic)
            .setObjectLayer(Layers.MOVING)
            .setShape(BoxShape(size.x.toFloat(), size.y.toFloat(), size.z.toFloat()))
            .setAngularDamping(0.1f)
            .setLinearDamping(0.3f)
            .setPosition(positionRVec3)
        return addObject(bodyCreationSettings)
    }

    fun addObject(bodyCreationSettings: BodyCreationSettings): Aboba {
        val aboba = Aboba(bodyCreationSettings)
        bodies[aboba.body.va()] = aboba
        return aboba
    }

    fun removeObject(body: Aboba) {
        body.remove()
        bodies.remove(body.body.va())
    }

    inner class Aboba(
        bodySettings: BodyCreationSettings,
    ) {
        val body: Body

        init {
            this.body = physicsSystem.getBodyInterface().createBody(bodySettings)
            physicsSystem.getBodyInterface().addBody(body, EActivation.Activate)
        }

        fun getTransform(): Transform {
            val rVec3 = RVec3()
            val quat = Quat()
            body.getPositionAndRotation(rVec3, quat)
            return Transform(
                Vec3D(rVec3.xx(), rVec3.yy(), rVec3.zz()),
                QRot(quat.w.toDouble(), quat.x.toDouble(), quat.y.toDouble(), quat.z.toDouble())
            )
        }

        internal fun remove() {
            physicsSystem.getBodyInterface().removeBody(body.id)
            physicsSystem.getBodyInterface().destroyBody(body.id)
        }
    }
}