package io.github.dockyardmc.world.chunk

import io.github.dockyardmc.tide.codec.CodecUtils.byteBufBytes
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.MapStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.world.block.BlockEntity
import io.netty.buffer.ByteBuf

data class ChunkData(
    val heightmaps: Map<ChunkHeightmap.Type, LongArray>,
    val sections: List<ChunkSection>,
    val blockEntities: List<BlockEntity>,
) {
    companion object {
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