package io.github.dockyardmc.protocol.plugin.messages

import io.github.dockyardmc.tide.stream.StreamCodec

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