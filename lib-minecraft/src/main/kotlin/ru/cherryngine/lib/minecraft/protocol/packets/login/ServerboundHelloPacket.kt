package ru.cherryngine.lib.minecraft.protocol.packets.login

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
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