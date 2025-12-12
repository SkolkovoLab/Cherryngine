package ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.network.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ClientboundSetPassengersPacket(
    val vehicle: Int,
    val passengers: List<Int>
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetPassengersPacket::vehicle,
            StreamCodec.VAR_INT.list(), ClientboundSetPassengersPacket::passengers,
            ::ClientboundSetPassengersPacket
        )
    }
}