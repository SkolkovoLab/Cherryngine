package ru.cherryngine.platform.minecraft.java.entity

import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.Metadata
import net.minestom.server.entity.attribute.Attribute
import net.minestom.server.network.packet.server.play.*
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.java.player.MinecraftPlayer
import ru.cherryngine.platform.minecraft.java.utils.ChunkUtils
import ru.cherryngine.platform.minecraft.java.utils.minestomPos
import ru.cherryngine.platform.minecraft.java.utils.minestomVec
import ru.cherryngine.platform.minecraft.java.view.Viewable
import ru.cherryngine.platform.minecraft.java.world.ChunkPos
import java.util.*

class McEntity(
    val entityId: Int,
    val entityType: EntityType,
) : Viewable {
    /**
     * Сырые entity-метаданные по индексам Minestom-а.
     * TODO: тонкая обёртка поверх Minestom MetadataHolder для типизированного доступа.
     */
    val metadata: MutableMap<Int, Metadata.Entry<*>> = mutableMapOf()
    var position = Vec3D.ZERO
    var yawPitch = YawPitch.ZERO
    private val viewers = mutableSetOf<MinecraftPlayer>()

    override var viewerPredicate: (MinecraftPlayer) -> Boolean = { true }
    var viewContextIDs: Set<String> = emptySet()

    override val chunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(position)

    val attributes: MutableMap<Attribute, Double> = mutableMapOf()

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        this.position = position
        this.yawPitch = yawPitch
        val packet = EntityPositionSyncPacket(
            entityId,
            position.minestomVec(),
            Vec.ZERO,
            yawPitch.yaw,
            yawPitch.pitch,
            false
        )
        viewers.forEach { it.connection.sendPacket(packet) }
    }

    fun resendMeta() {
        val packet = EntityMetaDataPacket(entityId, metadata.toMap())
        viewers.forEach { it.connection.sendPacket(packet) }
    }

    override fun show(player: MinecraftPlayer) {
        player.connection.sendPacket(
            SpawnEntityPacket(
                entityId, UUID.randomUUID(),
                entityType,
                position.minestomPos(yawPitch),
                yawPitch.yaw,
                0,
                Vec.ZERO
            )
        )
        player.connection.sendPacket(EntityMetaDataPacket(entityId, metadata.toMap()))
        if (attributes.isNotEmpty()) player.connection.sendPacket(
            EntityAttributesPacket(
                entityId,
                attributes.entries.map { (attr, value) ->
                    EntityAttributesPacket.Property(attr, value, emptyList())
                }
            )
        )
        viewers.add(player)
    }

    override fun hide(player: MinecraftPlayer) {
        player.connection.sendPacket(DestroyEntitiesPacket(listOf(entityId)))
        viewers.remove(player)
    }
}
