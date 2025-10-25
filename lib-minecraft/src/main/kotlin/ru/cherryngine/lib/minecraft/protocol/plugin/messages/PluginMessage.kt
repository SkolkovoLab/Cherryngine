package ru.cherryngine.lib.minecraft.protocol.plugin.messages

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

interface PluginMessage {
    data class Contents(
        val channel: String,
        val data: ByteBuf
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Contents::channel,
                StreamCodec.RAW_BYTES, Contents::data,
                ::Contents
            )
        }
    }
}