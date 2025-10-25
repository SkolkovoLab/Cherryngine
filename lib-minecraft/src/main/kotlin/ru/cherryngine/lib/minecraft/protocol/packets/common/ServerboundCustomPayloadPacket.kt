package ru.cherryngine.lib.minecraft.protocol.packets.common

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.plugin.messages.PluginMessage
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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