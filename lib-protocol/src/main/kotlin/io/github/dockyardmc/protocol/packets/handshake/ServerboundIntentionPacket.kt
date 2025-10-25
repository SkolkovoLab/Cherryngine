package io.github.dockyardmc.protocol.packets.handshake

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf

data class ServerboundIntentionPacket(
    val version: Int,
    val serverAddress: String,
    val port: Short,
    val intent: Intent
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ServerboundIntentionPacket::version,
            StreamCodec.STRING, ServerboundIntentionPacket::serverAddress,
            StreamCodec.SHORT, ServerboundIntentionPacket::port,
            Intent.STREAM_CODEC, ServerboundIntentionPacket::intent,
            ::ServerboundIntentionPacket
        )
    }

    enum class Intent(val id: Int) {
        STATUS(1),
        LOGIN(2),
        TRANSFER(3);

        companion object {
            fun fromId(id: Int): Intent {
                return entries.first { it.id == id }
            }

            val STREAM_CODEC = object : StreamCodec<Intent> {
                override fun write(buffer: ByteBuf, value: Intent) {
                    StreamCodec.VAR_INT.write(buffer, value.id)
                }

                override fun read(buffer: ByteBuf): Intent {
                    return fromId(StreamCodec.VAR_INT.read(buffer))
                }
            }
        }
    }
}