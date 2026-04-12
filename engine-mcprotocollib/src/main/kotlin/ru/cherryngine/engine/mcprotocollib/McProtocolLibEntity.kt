package ru.cherryngine.engine.mcprotocollib

import org.cloudburstmc.math.vector.Vector3d
import org.geysermc.mcprotocollib.protocol.data.game.entity.metadata.EntityMetadata
import org.geysermc.mcprotocollib.protocol.data.game.entity.type.EntityType
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundAddEntityPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundRemoveEntitiesPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityDataPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundTeleportEntityPacket
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import java.util.*

class McProtocolLibEntity(
    val entityId: Int,
    val entityType: EntityType,
) {
    var position: Vec3D = Vec3D.ZERO
    var yawPitch: YawPitch = YawPitch.ZERO
    var viewContextIDs: Set<String> = emptySet()
    var viewerPredicate: (McProtocolLibPlayer) -> Boolean = { true }

    private val viewers = mutableSetOf<McProtocolLibPlayer>()
    private val metadata = mutableMapOf<Int, EntityMetadata<*, *>>()

    val chunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(position)

    fun setMetadata(index: Int, entry: EntityMetadata<*, *>) {
        metadata[index] = entry
    }

    fun getMetadataArray(): Array<EntityMetadata<*, *>> = metadata.values.toTypedArray()

    fun show(player: McProtocolLibPlayer) {
        if (!viewers.add(player)) return
        player.session.send(
            ClientboundAddEntityPacket(
                entityId, UUID.randomUUID(), entityType,
                position.x, position.y, position.z,
                yawPitch.yaw.toFloat(), yawPitch.pitch.toFloat(), yawPitch.yaw.toFloat()
            )
        )
        if (metadata.isNotEmpty()) {
            player.session.send(ClientboundSetEntityDataPacket(entityId, getMetadataArray()))
        }
    }

    fun hide(player: McProtocolLibPlayer) {
        if (!viewers.remove(player)) return
        player.session.send(ClientboundRemoveEntitiesPacket(intArrayOf(entityId)))
    }

    fun teleport(pos: Vec3D, yp: YawPitch) {
        position = pos
        yawPitch = yp
        val packet = ClientboundTeleportEntityPacket(
            entityId,
            Vector3d.from(pos.x, pos.y, pos.z),
            Vector3d.ZERO,
            yp.yaw.toFloat(), yp.pitch.toFloat(),
            emptyList(), false
        )
        viewers.forEach { it.session.send(packet) }
    }

    fun resendMeta() {
        if (metadata.isEmpty()) return
        val packet = ClientboundSetEntityDataPacket(entityId, getMetadataArray())
        viewers.forEach { it.session.send(packet) }
    }
}
