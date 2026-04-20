package ru.cherryngine.engine.minecraft.entity

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.Metadata
import net.minestom.server.entity.attribute.Attribute as MinestomAttribute
import net.minestom.server.entity.EntityType as MinestomEntityType
import net.minestom.server.network.packet.server.play.DestroyEntitiesPacket
import net.minestom.server.network.packet.server.play.EntityAttributesPacket
import net.minestom.server.network.packet.server.play.EntityMetaDataPacket
import net.minestom.server.network.packet.server.play.EntityPositionSyncPacket
import net.minestom.server.network.packet.server.play.SpawnEntityPacket
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.engine.minecraft.view.Viewable
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.entity.MetadataContainer
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.types.Attribute
import ru.cherryngine.lib.minecraft.registry.types.EntityType
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import java.util.UUID

class McEntity(
    val entityId: Int,
    val entityType: EntityType,
) : Viewable {
    val metadata = MetadataContainer()
    var position = Vec3D.ZERO
    var yawPitch = YawPitch.ZERO
    private val viewers = mutableSetOf<MinecraftPlayer>()

    override var viewerPredicate: (MinecraftPlayer) -> Boolean = { true }
    var viewContextIDs: Set<String> = emptySet()

    override val chunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(position)

    val attributes: MutableMap<Attribute, Double> = mutableMapOf()

    private val minestomEntityType: MinestomEntityType by lazy {
        requireNotNull(MinestomEntityType.fromKey(entityType.key)) {
            "Unknown Minestom EntityType for key: ${entityType.key}"
        }
    }

    fun teleport(position: Vec3D, yawPitch: YawPitch) {
        this.position = position
        this.yawPitch = yawPitch
        val packet = EntityPositionSyncPacket(
            entityId,
            Vec(position.x, position.y, position.z),
            Vec.ZERO,
            yawPitch.yaw,
            yawPitch.pitch,
            false
        )
        viewers.forEach { it.connection.sendPacket(packet) }
    }

    fun resendMeta() {
        val packet = EntityMetaDataPacket(entityId, convertMetadata())
        viewers.forEach { it.connection.sendPacket(packet) }
    }

    override fun show(player: MinecraftPlayer) {
        player.connection.sendPacket(
            SpawnEntityPacket(
                entityId, UUID.randomUUID(),
                minestomEntityType,
                Pos(position.x, position.y, position.z, yawPitch.yaw, yawPitch.pitch),
                yawPitch.yaw,
                0,
                Vec.ZERO
            )
        )
        player.connection.sendPacket(EntityMetaDataPacket(entityId, convertMetadata()))
        if (attributes.isNotEmpty()) player.connection.sendPacket(
            EntityAttributesPacket(
                entityId,
                attributes.entries.mapNotNull { (attr, value) ->
                    val minestomAttr = MinestomAttribute.fromKey(attr.key) ?: return@mapNotNull null
                    EntityAttributesPacket.Property(minestomAttr, value, emptyList())
                }
            )
        )
        viewers.add(player)
    }

    override fun hide(player: MinecraftPlayer) {
        player.connection.sendPacket(DestroyEntitiesPacket(listOf(entityId)))
        viewers.remove(player)
    }

    // TODO: миграция MetadataContainer на Minestom Metadata.Entry<*>.
    // Сейчас метаданные не сериализуются — пакет улетает пустым.
    private fun convertMetadata(): Map<Int, Metadata.Entry<*>> = emptyMap()
}
