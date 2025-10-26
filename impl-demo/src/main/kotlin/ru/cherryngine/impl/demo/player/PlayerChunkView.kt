package ru.cherryngine.impl.demo.player

import ru.cherryngine.impl.demo.world.Chunk
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundSetChunkCacheCenterPacket
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class PlayerChunkView(
    val player: Player,
    val chunks: Map<Long, Chunk>,
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 3
    }

    private var previousChunkPos = ChunkPos.ZERO

    fun init() {
        val currentChunkPos = player.clientChunkPos
        previousChunkPos = currentChunkPos
        val distance = DEFAULT_RENDER_DISTANCE
        player.sendPacket(ClientboundSetChunkCacheCenterPacket(currentChunkPos))
        ChunkUtils.getChunksInRange(player.clientChunkPos, distance).forEach {
            loadChunk(it)
        }
    }

    fun update() {
        val currentChunkPos = player.clientChunkPos
        if (previousChunkPos == currentChunkPos) return

        val distance = DEFAULT_RENDER_DISTANCE

        player.sendPacket(ClientboundSetChunkCacheCenterPacket(currentChunkPos))

        // new chunks
        val chunksToLoad =
            ChunkUtils.forDifferingChunksInRange(
                currentChunkPos,
                previousChunkPos,
                distance
            )
        val chunksToUnload =
            ChunkUtils.forDifferingChunksInRange(
                previousChunkPos,
                currentChunkPos,
                distance
            )

        chunksToLoad.forEach(::loadChunk)
        chunksToUnload.forEach(::unloadChunk)

        previousChunkPos = currentChunkPos
    }

    fun resendChunks() {
        val distance = DEFAULT_RENDER_DISTANCE
        ChunkUtils.getChunksInRange(player.clientChunkPos, distance).forEach {
            loadChunk(it)
        }
    }

    fun loadChunk(pos: ChunkPos) {
        val chunk = chunks[pos.pack()] ?: Chunk.EMPTY
        player.sendPacket(ClientboundLevelChunkWithLightPacket(pos, chunk.chunkData, chunk.light))
    }

    fun unloadChunk(pos: ChunkPos) {
        player.sendPacket(ClientboundForgetLevelChunkPacket(pos))
    }
}