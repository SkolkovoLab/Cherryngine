package ru.cherryngine.lib.minecraft.protocol.packets.login

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundLoginCustomQueryPacket(
    val messageId: Int,
    val channel: String,
    val data: ByteBuf
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundLoginCustomQueryPacket::messageId,
            StreamCodec.STRING, ClientboundLoginCustomQueryPacket::channel,
            StreamCodec.RAW_BYTES, ClientboundLoginCustomQueryPacket::data,
            ::ClientboundLoginCustomQueryPacket
        )
    }
}