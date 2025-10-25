package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetDisplayObjectivePacket(
    val position: ObjectivePosition,
    val text: String
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<ObjectivePosition>(), ClientboundSetDisplayObjectivePacket::position,
            StreamCodec.STRING, ClientboundSetDisplayObjectivePacket::text,
            ::ClientboundSetDisplayObjectivePacket
        )
    }

    enum class ObjectivePosition {
        LIST,
        SIDEBAR,
        BELOW_NAME
    }
}