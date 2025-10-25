package ru.cherryngine.lib.minecraft.protocol.packets.login

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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