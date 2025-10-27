package ru.cherryngine.impl.demo.player

import net.kyori.adventure.text.Component
import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.world.world.World
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.data.components.AxolotlVariantComponent
import ru.cherryngine.lib.minecraft.entity.MetadataDef
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.AxolotlVariant
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.CatVariants
import ru.cherryngine.lib.minecraft.registry.EntityTypes
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import kotlin.random.Random

class Player(
    val connection: Connection,
) {
    val clientPosition: Vec3D
        get() = lastPosition

    val clientYawPitch: YawPitch
        get() = lastYawPitch

    val clientChunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(clientPosition)

    var lastPosition: Vec3D = Vec3D.ZERO
    var lastYawPitch: YawPitch = YawPitch.ZERO

    fun sendPacket(packet: ClientboundPacket) {
        connection.sendPacket(packet)
    }

    val currentVisibleEntities = mutableSetOf<McEntity>()

    var playerChunkView: PlayerChunkView? = null
    var playerEntityView: PlayerEntityView? = null

    val entity = McEntity(Random.nextInt(1000, 1_000_000), EntityTypes.AXOLOTL).apply {
        metadata[MetadataDef.Axolotl.HAS_NO_GRAVITY] = true
        metadata[MetadataDef.Axolotl.VARIANT] = AxolotlVariant.entries.random()
        metadata[MetadataDef.Axolotl.CUSTOM_NAME] = Component.text(connection.address.toString())
        metadata[MetadataDef.Axolotl.CUSTOM_NAME_VISIBLE] = true
        viewerPredicate = { it != this@Player }
    }

    var world: World? = null
        set(value) {
            val oldValue = field
            field = value
            oldValue?.mutableEntities -= entity
            value?.mutableEntities += entity
            if (value != null) {
                playerChunkView = PlayerChunkView(this, value.chunks).apply { init() }
                playerEntityView = PlayerEntityView(this, { value.entities }).apply { init() }
            } else {
                playerChunkView = null
                playerEntityView = null
            }
        }

    fun tick() {
        entity.teleport(clientPosition, clientYawPitch)
        playerChunkView?.update()
        playerEntityView?.update()
    }
}