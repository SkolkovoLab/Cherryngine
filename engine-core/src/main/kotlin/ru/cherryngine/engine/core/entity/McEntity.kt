package ru.cherryngine.engine.core.entity

import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.view.Viewable
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.entity.MetadataContainer
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.*
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.TeleportFlags
import ru.cherryngine.lib.minecraft.r2.Attribute
import ru.cherryngine.lib.minecraft.registry.entries.EntityType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import java.util.*

class McEntity(
    val entityId: Int,
    val entityType: EntityType,
) : Viewable {
    val metadata = MetadataContainer()
    var position = Vec3D.ZERO
    var yawPitch = YawPitch.ZERO
    private val viewers = mutableSetOf<Player>()

    override var viewerPredicate: (Player) -> Boolean = { true }

    override val chunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(position)

    val attributes: MutableMap<Attribute, Double> = mutableMapOf()

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        this.position = position
        this.yawPitch = yawPitch
        val packet =
            ClientboundTeleportEntityPacket(entityId, position, Vec3D.ZERO, yawPitch, TeleportFlags.EMPTY, false)
        viewers.forEach { it.connection.sendPacket(packet) }
    }

    fun resendMeta() {
        val packet = ClientboundSetEntityDataPacket(entityId, metadata.entries)
        viewers.forEach { it.connection.sendPacket(packet) }
    }

    override fun show(player: Player) {
        player.connection.sendPacket(
            ClientboundAddEntityPacket(
                entityId, UUID.randomUUID(),
                entityType,
                position,
                yawPitch, yawPitch.yaw,
                0,
                Vec3D.ZERO
            )
        )
        player.connection.sendPacket(ClientboundSetEntityDataPacket(entityId, metadata.entries))
        if (attributes.isNotEmpty()) player.connection.sendPacket(
            ClientboundUpdateAttributesPacket(
                entityId,
                attributes.entries.map { ClientboundUpdateAttributesPacket.Property(it.key, it.value, listOf()) })
        )
        viewers.add(player)
    }

    override fun hide(player: Player) {
        player.connection.sendPacket(ClientboundRemoveEntitiesPacket(listOf(entityId)))
        viewers.remove(player)
    }
}