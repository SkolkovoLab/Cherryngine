package ru.cherryngine.impl.demo

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.ChunkPos
import io.github.dockyardmc.server.Connection
import io.github.dockyardmc.utils.ChunkUtils
import ru.cherryngine.lib.math.Vec3D

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