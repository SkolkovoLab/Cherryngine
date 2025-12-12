package ru.cherryngine.lib.minecraft.network.protocol.plugin.messages

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

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