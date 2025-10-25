package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.protocol.types.EntityAnimation
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundAnimatePacket(
    val entityId: Int,
    val animation: EntityAnimation
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundAnimatePacket::entityId,
            EnumStreamCodec<EntityAnimation>(), ClientboundAnimatePacket::animation,
            ::ClientboundAnimatePacket
        )
    }
}
