package io.github.dockyardmc.protocol.packets.login

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import java.util.*

data class ServerboundHelloPacket(
    val name: String,
    val uuid: UUID
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ServerboundHelloPacket::name,
            StreamCodec.UUID, ServerboundHelloPacket::uuid,
            ::ServerboundHelloPacket
        )
    }
}