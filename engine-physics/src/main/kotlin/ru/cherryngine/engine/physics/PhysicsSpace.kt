package ru.cherryngine.engine.physics

import com.github.stephengold.joltjni.*
import com.github.stephengold.joltjni.enumerate.EActivation
import com.github.stephengold.joltjni.enumerate.EMotionType
import com.github.stephengold.joltjni.enumerate.EPhysicsUpdateError
import com.github.stephengold.joltjni.readonly.ConstPlane
import com.github.stephengold.joltjni.readonly.ConstShape
import com.github.stephengold.joltjni.readonly.Vec3Arg
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D


class PhysicsSpace {
    val physicsSystem: PhysicsSystem
    val tempAllocator: TempAllocator
    val jobSystem: JobSystem

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
    }

    fun addFloor(y: Double): PhysicsBody {
        val normal: Vec3Arg = Vec3.sAxisY()
        val plane: ConstPlane = Plane(normal, -y.toFloat())
        val floorShape: ConstShape = PlaneShape(plane)

        val bodyCreationSettings = BodyCreationSettings()
            .setMotionType(EMotionType.Static)
            .setObjectLayer(Layers.NON_MOVING)
            .setShape(floorShape)

        return createBody(bodyCreationSettings, EActivation.DontActivate)
    }

    fun update(delta: Float) {
        val steps = 1
        physicsSystem.update(delta, steps, tempAllocator, jobSystem).also { errors ->
            check(errors == EPhysicsUpdateError.None) { errors }
        }
    }

    val bodies = HashMap<Long, PhysicsBody>()

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

    fun createBody(bodyCreationSettings: BodyCreationSettings, eActivation: EActivation): PhysicsBody {
        val physicsBody = PhysicsBody(bodyCreationSettings, eActivation)
        bodies[physicsBody.body.va()] = physicsBody
        return physicsBody
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

        fun remove() {
            physicsSystem.getBodyInterface().removeBody(body.id)
            physicsSystem.getBodyInterface().destroyBody(body.id)
            bodies.remove(body.va())
        }
    }
}