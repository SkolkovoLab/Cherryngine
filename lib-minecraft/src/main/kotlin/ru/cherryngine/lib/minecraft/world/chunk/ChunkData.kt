package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.stream_codec.MapStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.entries.DimensionType
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import java.util.*

class ChunkData(
    val heightmaps: Map<ChunkHeightmapType, LongArray>,
    val sections: List<ChunkSection>,
    val blockEntities: Map<Vec3I, BlockEntity>,
) {
    companion object {
        fun empty(dimensionType: DimensionType) = ChunkData(
            emptyMap(),
            List(dimensionType.height / 16) { ChunkSection.empty() },
            emptyMap(),
        )

        val HEIGHTMAPS_STREAM_CODEC = MapStreamCodec(ChunkHeightmapType.STREAM_CODEC, StreamCodec.LONG_ARRAY) {
            EnumMap<ChunkHeightmapType, LongArray>(ChunkHeightmapType::class.java)
        }

        val STREAM_CODEC = StreamCodec.of(
            HEIGHTMAPS_STREAM_CODEC, ChunkData::heightmaps,
            ChunkSection.STREAM_CODEC_LIST, ChunkData::sections,
            BlockEntity.STREAM_CODEC_MAP, ChunkData::blockEntities,
            ::ChunkData
        )
    }
}