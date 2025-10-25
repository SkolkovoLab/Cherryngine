package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.Vec3D
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetEntityMotionPacket(
    val entityId: Int,
    val velocity: Vec3D
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundSetEntityMotionPacket::entityId,
            LocationCodecs.VELOCITY, ClientboundSetEntityMotionPacket::velocity,
            ::ClientboundSetEntityMotionPacket
        )
    }
}