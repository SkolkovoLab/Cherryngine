@file:Suppress("unused")

package ru.cherryngine.engine.core.entity

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.*
import net.minestom.server.entity.metadata.EntityMeta
import net.minestom.server.network.packet.server.ServerPacket
import net.minestom.server.network.packet.server.play.*
import ru.cherryngine.engine.core.ext.minestomPos
import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import java.util.*
import kotlin.reflect.KClass

class EngineEntity(
    val entityType: EntityType,
    val data: Int = 0,
) {
    val entityId: Int = Entity.generateId()
    val uuid: UUID = UUID.randomUUID()
    val metadata: MetadataHolder = MetadataHolder(null)

    var oldRotation: View = View.Companion.ZERO
    var rotation: View = View.Companion.ZERO

    var oldPosition: Vec3D = Vec3D.Companion.ZERO
    var position: Vec3D = Vec3D.Companion.ZERO

    private val minestomPos: Pos get() = position.minestomPos(rotation)

    var onGround: Boolean = false
    var velocity: Vec3D = Vec3D.Companion.ZERO

    private val packetVelocity: Vec3D get() = velocity * (8000.0 / 20.0)

    private val viewers: HashSet<ClientConnection> = hashSetOf()

    fun <T : EntityMeta> editEntityMeta(metaClass: KClass<T>, editor: (T) -> Unit) {
        editEntityMeta {
            val constructor = metaClass.constructors.first()

            @Suppress("USELESS_CAST")
            val meta = constructor.call(null, it) as T
            editor(meta)
        }
    }

    fun editEntityMeta(editor: (MetadataHolder) -> Unit) {
        editor(metadata)
        val metaPacket = metaPacket
        viewers.forEach { it.sendPacket(metaPacket) }
    }

    fun updatePositionAndRotation(
        newPosition: Vec3D? = null,
        newRotation: View? = null,
    ) {
        var positionChanged = false
        if (newPosition != null && position != newPosition) {
            oldPosition = position
            position = newPosition
            if ((oldPosition - position).length() > 8) oldPosition = position
            positionChanged = true
        }

        var rotationChanged = false
        if (newRotation != null && rotation != newRotation) {
            oldRotation = rotation
            rotation = newRotation
            rotationChanged = true
        }

        when {
            positionChanged && rotationChanged -> arrayOf(positionAndRotationPacket, headLookPacket)
            positionChanged -> arrayOf(positionPacket)
            rotationChanged -> arrayOf(rotationPacket, headLookPacket)
            else -> null
        }?.let { packets ->
            viewers.forEach { it.sendPackets(*packets) }
        }
    }

    private val spawnPacket: ServerPacket.Play
        get() {
            return if (entityType.registry().spawnType() == EntitySpawnType.EXPERIENCE_ORB) {
                SpawnExperienceOrbPacket(entityId, minestomPos, data.toShort())
            } else {
                val packetVelocity = packetVelocity
                SpawnEntityPacket(
                    entityId,
                    uuid,
                    entityType.id(),
                    minestomPos,
                    rotation.yaw,
                    data,
                    packetVelocity.x.toInt().toShort(),
                    packetVelocity.y.toInt().toShort(),
                    packetVelocity.z.toInt().toShort(),
                )
            }
        }

    private val metaPacket: EntityMetaDataPacket
        get() = EntityMetaDataPacket(entityId, metadata.entries)

    private val teleportPacket: EntityTeleportPacket
        get() = EntityTeleportPacket(entityId, minestomPos, Vec.ZERO, RelativeFlags.DELTA, onGround)

    private val destroyPacket: DestroyEntitiesPacket
        get() = DestroyEntitiesPacket(entityId)

    val positionPacket: ServerPacket.Play
        get() {
            val diff = position - oldPosition
//            return if ((diff).length() > 8) {
//                EntityPositionSyncPacket(entityId, minestomPos, MinestomVec.ZERO, rotation.yaw, rotation.pitch, onGround)
//            } else {
//                EntityPositionPacket.getPacket(entityId, minestomPos, oldPosition.minestomPos(rotation), onGround)
//            }
            return teleportPacket
        }

    val rotationPacket: ServerPacket.Play
        get() = EntityRotationPacket(entityId, rotation.yaw, rotation.pitch, onGround)

    val headLookPacket: ServerPacket.Play
        get() = EntityHeadLookPacket(entityId, rotation.yaw)


    val positionAndRotationPacket: ServerPacket.Play
        get() {
            val diff = position - oldPosition
//           return if ((diff).length() > 8) {
//                EntityPositionSyncPacket(entityId, minestomPos, MinestomVec.ZERO, rotation.yaw, rotation.pitch, onGround)
//            } else {
//                EntityPositionAndRotationPacket.getPacket(entityId, minestomPos, oldPosition.minestomPos(rotation), onGround)
//            }
            return teleportPacket
        }
    fun show(viewer: ClientConnection) {
        this.viewers.add(viewer)
        viewer.sendPackets(spawnPacket, metaPacket)
    }

    fun hide(viewer: ClientConnection) {
        viewer.sendPacket(destroyPacket)
        this.viewers.remove(viewer)
    }

    fun remove() {
        val destroyPacket = destroyPacket
        viewers.forEach { it.sendPacket(destroyPacket) }
        viewers.clear()
    }
}