package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf

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