package ru.cherryngine.lib.minecraft.protocol.packets.login

import io.netty.buffer.ByteBuf
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ServerboundCustomQueryAnswerPacket(
    val messageId: Int,
    val data: ByteBuf? = null
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundCustomQueryAnswerPacket::messageId,
            StreamCodec.RAW_BYTES.optional(), ServerboundCustomQueryAnswerPacket::data,
            ::ServerboundCustomQueryAnswerPacket
        )
    }
}