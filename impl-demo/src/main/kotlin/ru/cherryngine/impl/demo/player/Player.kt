package ru.cherryngine.impl.demo.player

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.world.world.World
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

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

    var world: World? = null
        set(value) {
            field = value
            if (value != null) {
                playerChunkView = PlayerChunkView(this, value.chunks).apply { init() }
                playerEntityView = PlayerEntityView(this, { value.entities }).apply { init() }
            } else {
                playerChunkView = null
                playerEntityView = null
            }
        }

    fun tick() {
        playerChunkView?.update()
        playerEntityView?.update()
    }
}