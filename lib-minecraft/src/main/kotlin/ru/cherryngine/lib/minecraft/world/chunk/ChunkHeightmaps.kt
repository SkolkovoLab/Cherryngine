package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.minecraft.tide.stream.MapStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import java.util.*

class ChunkHeightmaps(
    val heightmaps: EnumMap<ChunkHeightmap.Type, ChunkHeightmap> = EnumMap(ChunkHeightmap.Type::class.java),
) {
    fun getOrCreateHeightmap(chunk: Chunk, type: ChunkHeightmap.Type): ChunkHeightmap =
        heightmaps.computeIfAbsent(type) { ChunkHeightmap(chunk, type) }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(ChunkHeightmap.Type.STREAM_CODEC, ChunkHeightmap.STREAM_CODEC) {
                EnumMap<ChunkHeightmap.Type, ChunkHeightmap>(ChunkHeightmap.Type::class.java)
            }, ChunkHeightmaps::heightmaps
        ) {
            ChunkHeightmaps(it as EnumMap<ChunkHeightmap.Type, ChunkHeightmap>)
        }
    }
}