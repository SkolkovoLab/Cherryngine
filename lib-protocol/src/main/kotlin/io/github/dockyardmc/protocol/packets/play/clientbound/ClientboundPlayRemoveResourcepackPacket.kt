package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import java.util.*

data class ClientboundPlayRemoveResourcepackPacket(
    val uuid: UUID?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.UUID.optional(), ClientboundPlayRemoveResourcepackPacket::uuid,
            ::ClientboundPlayRemoveResourcepackPacket
        )
    }
}