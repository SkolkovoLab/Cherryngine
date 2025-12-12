package ru.cherryngine.lib.minecraft.network.protocol.packets.login

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundLoginCompressionPacket(
    val compression: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundLoginCompressionPacket::compression,
            ::ClientboundLoginCompressionPacket
        )
    }
}