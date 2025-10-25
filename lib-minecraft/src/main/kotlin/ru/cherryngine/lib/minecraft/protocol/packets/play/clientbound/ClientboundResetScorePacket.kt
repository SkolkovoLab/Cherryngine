package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ClientboundResetScorePacket(
    val name: String,
    val objective: String?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundResetScorePacket::name,
            StreamCodec.STRING.optional(), ClientboundResetScorePacket::objective,
            ::ClientboundResetScorePacket
        )
    }
}