package ru.cherryngine.impl.demo.player

import ru.cherryngine.impl.demo.world.PlayerChunkView
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class Player(
    val connection: Connection,
) {
    val clientPosition: Vec3D
        get() = lastPosition

    val clientChunkPos: ChunkPos
        get() = ChunkUtils.chunkPosFromVec3D(clientPosition)

    var lastPosition: Vec3D = Vec3D.ZERO

    fun sendPacket(packet: ClientboundPacket) {
        connection.sendPacket(packet)
    }

    var playerChunkView: PlayerChunkView? = null

    fun tick() {
        playerChunkView?.update()
    }
}