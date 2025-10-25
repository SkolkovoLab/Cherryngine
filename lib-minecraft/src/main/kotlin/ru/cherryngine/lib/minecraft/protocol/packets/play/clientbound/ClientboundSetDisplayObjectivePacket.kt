package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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