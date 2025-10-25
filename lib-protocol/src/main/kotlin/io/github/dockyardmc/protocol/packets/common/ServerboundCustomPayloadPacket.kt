package io.github.dockyardmc.protocol.packets.common

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.protocol.plugin.messages.PluginMessage
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundCustomPayloadPacket(
    val contents: PluginMessage.Contents
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.Companion.of(
            PluginMessage.Contents.STREAM_CODEC, ServerboundCustomPayloadPacket::contents,
            ::ServerboundCustomPayloadPacket
        )
    }
}