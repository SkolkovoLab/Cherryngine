package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundMoveVehiclePacket(
    val position: Vec3D,
    val view: View
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            LocationCodecs.VEC_3D, ClientboundMoveVehiclePacket::position,
            LocationCodecs.VIEW, ClientboundMoveVehiclePacket::view,
            ::ClientboundMoveVehiclePacket
        )
    }
}