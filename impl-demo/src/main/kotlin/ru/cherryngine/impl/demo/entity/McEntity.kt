package ru.cherryngine.impl.demo.entity

import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.entity.MetadataContainer
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundAddEntityPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundRemoveEntitiesPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSetEntityDataPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundTeleportEntityPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.protocol.types.predicate.BlockPredicate
import ru.cherryngine.lib.minecraft.registry.registries.EntityType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import java.util.*

class McEntity(
    val entityId: Int,
    val entityType: EntityType,
) {
    val metadata = MetadataContainer()
    var position = Vec3D.ZERO
    var yawPitch = YawPitch.ZERO
    private val viewers = mutableSetOf<Player>()

    var viewerPredicate: (Player) -> Boolean = { true }

    val chunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(position)

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        this.position = position
        this.yawPitch = yawPitch
        val packet = ClientboundTeleportEntityPacket(entityId, position, Vec3D.ZERO, yawPitch, TeleportFlags.EMPTY, false)
        viewers.forEach { it.sendPacket(packet) }
    }

    fun show(player: Player) {
        player.sendPacket(
            ClientboundAddEntityPacket(
                entityId, UUID.randomUUID(),
                entityType,
                position,
                yawPitch, yawPitch.yaw,
                0,
                Vec3D.ZERO
            )
        )
        player.sendPacket(ClientboundSetEntityDataPacket(entityId, metadata.entries))
        viewers.add(player)
    }

    fun hide(player: Player) {
        player.sendPacket(ClientboundRemoveEntitiesPacket(listOf(entityId)))
        viewers.remove(player)
    }
}