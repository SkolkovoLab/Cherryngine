package ru.cherryngine.engine.mcprotocollib

import io.netty.buffer.Unpooled
import org.geysermc.mcprotocollib.protocol.data.game.level.HeightmapTypes
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import java.util.EnumMap

object McProtocolLibChunkSerializer {
    /**
     * Serializes chunk sections to raw bytes using the engine's ChunkSection.STREAM_CODEC.
     * Each section is written sequentially without any length prefix.
     * This matches the format expected by MCProtocolLib's ClientboundLevelChunkWithLightPacket.
     */
    fun serializeSections(sections: List<ChunkSection>): ByteArray {
        val buf = Unpooled.buffer()
        try {
            sections.forEach { section ->
                ChunkSection.STREAM_CODEC.write(buf, section)
            }
            val bytes = ByteArray(buf.readableBytes())
            buf.readBytes(bytes)
            return bytes
        } finally {
            buf.release()
        }
    }

    /**
     * Converts engine ChunkHeightmapType map to MCProtocolLib HeightmapTypes map.
     */
    fun convertHeightmaps(heightmaps: Map<ChunkHeightmapType, LongArray>): Map<HeightmapTypes, LongArray> {
        val result = EnumMap<HeightmapTypes, LongArray>(HeightmapTypes::class.java)
        heightmaps.forEach { (type, data) ->
            val mcplType = when (type) {
                ChunkHeightmapType.WORLD_SURFACE -> HeightmapTypes.WORLD_SURFACE
                ChunkHeightmapType.MOTION_BLOCKING -> HeightmapTypes.MOTION_BLOCKING
                ChunkHeightmapType.MOTION_BLOCKING_NO_LEAVES -> HeightmapTypes.MOTION_BLOCKING_NO_LEAVES
            }
            result[mcplType] = data
        }
        return result
    }
}
