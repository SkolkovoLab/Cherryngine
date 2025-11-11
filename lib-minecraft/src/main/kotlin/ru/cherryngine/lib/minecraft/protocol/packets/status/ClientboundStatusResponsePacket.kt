package ru.cherryngine.lib.minecraft.protocol.packets.status

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.ServerStatus
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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