package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

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