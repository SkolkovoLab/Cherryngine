package ru.cherryngine.lib.minecraft.protocol.plugin.messages

import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class BrandPluginMessage(
    val brand: String
) : PluginMessage {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, BrandPluginMessage::brand,
            ::BrandPluginMessage
        )
    }
}