package ru.cherryngine.lib.minecraft.network.protocol.packets.login

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import java.util.*

data class ServerboundHelloPacket(
    val username: String,
    val uuid: UUID
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ServerboundHelloPacket::username,
            StreamCodec.UUID, ServerboundHelloPacket::uuid,
            ::ServerboundHelloPacket
        )
    }
}