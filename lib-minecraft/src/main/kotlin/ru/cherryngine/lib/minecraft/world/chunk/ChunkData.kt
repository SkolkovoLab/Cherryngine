package ru.cherryngine.lib.minecraft.world.chunk

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils.byteBufBytes
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.MapStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.world.block.BlockEntity

data class ChunkData(
    val heightmaps: Map<ChunkHeightmap.Type, LongArray>,
    val sections: List<ChunkSection>,
    val blockEntities: List<BlockEntity>,
) {
    companion object {
        val EMPTY = ChunkData(emptyMap(), emptyList(), emptyList())

        val sectionsStreamCodec = object : StreamCodec<List<ChunkSection>> {
            override fun write(buffer: ByteBuf, value: List<ChunkSection>) {
                val data = byteBufBytes { b ->
                    value.forEach { section ->
                        ChunkSection.STREAM_CODEC.write(b, section)
                    }
                }
                StreamCodec.BYTE_ARRAY.write(buffer, data)
            }

            override fun read(buffer: ByteBuf): List<ChunkSection> {
                val data = StreamCodec.BYTE_ARRAY.read(buffer)
                TODO("Not yet implemented")
            }
        }

        val STREAM_CODEC = StreamCodec.of(
            MapStreamCodec(EnumStreamCodec<ChunkHeightmap.Type>(), StreamCodec.LONG_ARRAY), ChunkData::heightmaps,
            sectionsStreamCodec, ChunkData::sections,
            BlockEntity.STREAM_CODEC.list(), ChunkData::blockEntities,
            ::ChunkData
        )
    }
}