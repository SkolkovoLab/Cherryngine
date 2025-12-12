package ru.cherryngine.lib.minecraft.network.protocol.packets.status

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.ServerStatus
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundStatusResponsePacket(
    val serverStatus: ServerStatus,
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ServerStatus.STREAM_CODEC, ClientboundStatusResponsePacket::serverStatus,
            ::ClientboundStatusResponsePacket
        )
    }
}