package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.cherry.math.Vec3D
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundBlockDestructionPacket(
    val breakerId: Int,
    val location: Vec3D,
    val destroyStage: Byte
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundBlockDestructionPacket::breakerId,
            LocationCodecs.VEC_3D, ClientboundBlockDestructionPacket::location,
            StreamCodec.BYTE, ClientboundBlockDestructionPacket::destroyStage,
            ::ClientboundBlockDestructionPacket
        )
    }
}