package ru.cherryngine.impl.demo

import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundForgetLevelChunkPacket
import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundLevelChunkWithLightPacket
import io.github.dockyardmc.protocol.packets.play.clientbound.ClientboundSetChunkCacheCenterPacket
import io.github.dockyardmc.protocol.types.ChunkPos
import io.github.dockyardmc.utils.ChunkUtils
import io.github.dockyardmc.world.test.Chunk
import io.github.dockyardmc.world.test.World
import kotlin.math.floor
import kotlin.math.sqrt

class PlayerChunkView(
    val player: Player,
    val world: World,
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
        getChunksInRange(player.clientChunkPos, distance).forEach {
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
        getChunksInRange(player.clientChunkPos, distance).forEach {
            loadChunk(it)
        }
    }

    fun loadChunk(pos: ChunkPos) {
        val chunk = world.chunks[pos.pack()] ?: Chunk.EMPTY
        player.sendPacket(ClientboundLevelChunkWithLightPacket(pos, chunk.chunkData, chunk.light))
    }

    fun unloadChunk(pos: ChunkPos) {
        player.sendPacket(ClientboundForgetLevelChunkPacket(pos))
    }

    fun getChunksInRange(pos: ChunkPos, range: Int): List<ChunkPos> {
        val chunksInRange = (range * 2 + 1) * (range * 2 + 1)
        return List(chunksInRange) { i ->
            chunkInSpiral(i, pos.x, pos.z)
        }
    }

    fun chunkInSpiral(id: Int, xOffset: Int = 0, zOffset: Int = 0): ChunkPos {
        // if the id is 0 then we know we're in the centre
        if (id == 0) return ChunkPos(xOffset, zOffset)

        val index: Int = id - 1

        // compute radius (inverse arithmetic sum of 8 + 16 + 24 + ...)
        val radius: Int = floor((sqrt(index + 1.0) - 1) / 2).toInt() + 1

        // compute total point on radius -1 (arithmetic sum of 8 + 16 + 24 + ...)
        val p = 8 * radius * (radius - 1) / 2

        // points by face
        val en = radius * 2

        // compute de position and shift it so the first is (-r, -r) but (-r + 1, -r)
        // so the square can connect
        val a = (1 + index - p) % (radius * 8)

        return when (a / (radius * 2)) {
            // find the face (0 = top, 1 = right, 2 = bottom, 3 = left)
            0 -> ChunkPos(a - radius + xOffset, -radius + zOffset)
            1 -> ChunkPos(radius + xOffset, a % en - radius + zOffset)
            2 -> ChunkPos(radius - a % en + xOffset, radius + zOffset)
            3 -> ChunkPos(-radius + xOffset, radius - a % en + zOffset)
            else -> ChunkPos.ZERO
        }
    }
}