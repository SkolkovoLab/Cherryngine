package ru.cherryngine.integration.grim.player

import ac.grim.grimac.platform.api.world.PlatformChunk
import ac.grim.grimac.platform.api.world.PlatformWorld
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState
import ru.cherryngine.engine.core.Player
import ru.cherryngine.lib.math.Vec3I
import java.util.*

class PlatformWorldImpl(
    private val player: Player,
) : PlatformWorld {
    override fun isChunkLoaded(chunkX: Int, chunkZ: Int): Boolean {
        return true
    }

    override fun getBlockAt(
        x: Int,
        y: Int,
        z: Int,
    ): WrappedBlockState {
        val blockId = player.getBlockId(Vec3I(x, y, z))
        return WrappedBlockState.getByGlobalId(blockId)
    }

    override fun getName(): String {
        return player.username
    }

    override fun getUID(): UUID {
        return player.uuid
    }

    override fun getChunkAt(
        currChunkX: Int,
        currChunkZ: Int,
    ): PlatformChunk {
        return PlatformChunk { x, y, z ->
            val pos = Vec3I(x + currChunkX * 16, y, z + currChunkZ * 16)
            player.getBlockId(pos)
        }
    }

    override fun isLoaded(): Boolean {
        return true
    }
}