package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.plugin.messages.PluginMessage
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundCustomPayloadPacket(
    val contents: PluginMessage.Contents
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            PluginMessage.Contents.STREAM_CODEC, ClientboundCustomPayloadPacket::contents,
            ::ClientboundCustomPayloadPacket
        )
    }
}