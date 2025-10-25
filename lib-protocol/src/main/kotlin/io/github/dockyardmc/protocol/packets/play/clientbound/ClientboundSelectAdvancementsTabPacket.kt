package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.key.Key

data class ClientboundSelectAdvancementsTabPacket(
    val identifier: Key?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY.optional(), ClientboundSelectAdvancementsTabPacket::identifier,
            ::ClientboundSelectAdvancementsTabPacket
        )
    }
}