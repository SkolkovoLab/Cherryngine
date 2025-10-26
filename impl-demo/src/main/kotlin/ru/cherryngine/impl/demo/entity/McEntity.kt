package ru.cherryngine.impl.demo.entity

import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.entity.MetadataContainer
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundAddEntityPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundRemoveEntitiesPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSetEntityDataPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
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

    val chunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(position)

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
    }

    fun hide(player: Player) {
        player.sendPacket(ClientboundRemoveEntitiesPacket(listOf(entityId)))
    }
}