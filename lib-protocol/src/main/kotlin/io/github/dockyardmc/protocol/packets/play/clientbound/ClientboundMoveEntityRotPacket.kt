package io.github.dockyardmc.protocol.packets.play.clientbound

import ru.cherryngine.lib.math.View
import io.github.dockyardmc.codec.LocationCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundMoveEntityRotPacket(
    val entityId: Int,
    val view: View,
    val isOnGround: Boolean
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, ClientboundMoveEntityRotPacket::entityId,
            LocationCodecs.VIEW, ClientboundMoveEntityRotPacket::view,
            StreamCodec.BOOLEAN, ClientboundMoveEntityRotPacket::isOnGround,
            ::ClientboundMoveEntityRotPacket
        )
    }
}