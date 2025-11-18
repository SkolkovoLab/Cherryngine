package ru.cherryngine.lib.minecraft.world.chunk

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.BlockEntity

class ChunkData(
    val heightmaps: ChunkHeightmaps,
    val sections: List<ChunkSection>,
    val blockEntities: Map<Vec3I, BlockEntity>,
) {
    companion object {
        fun empty(dimensionType: DimensionType) = ChunkData(
            ChunkHeightmaps(),
            List(dimensionType.height / 16) { ChunkSection.empty() },
            emptyMap(),
        )

        val STREAM_CODEC = StreamCodec.of(
            ChunkHeightmaps.STREAM_CODEC, ChunkData::heightmaps,
            ChunkSection.STREAM_CODEC_LIST, ChunkData::sections,
            BlockEntity.STREAM_CODEC_MAP, ChunkData::blockEntities,
            ::ChunkData
        )
    }
}