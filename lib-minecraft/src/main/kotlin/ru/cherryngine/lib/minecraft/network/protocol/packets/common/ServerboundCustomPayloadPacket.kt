package ru.cherryngine.lib.minecraft.network.protocol.packets.common

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.plugin.messages.PluginMessage
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ServerboundCustomPayloadPacket(
    val contents: PluginMessage.Contents
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            PluginMessage.Contents.STREAM_CODEC, ServerboundCustomPayloadPacket::contents,
            ::ServerboundCustomPayloadPacket
        )
    }
}