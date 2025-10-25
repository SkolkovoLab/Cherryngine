package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.codec.ComponentCodecs
import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec
import net.kyori.adventure.text.Component

data class ClientboundSetScorePacket(
    val entityName: String,
    val objectiveName: String,
    val value: Int,
    val displayName: Component?,
    val numberFormat: Int?
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ClientboundSetScorePacket::entityName,
            StreamCodec.STRING, ClientboundSetScorePacket::objectiveName,
            StreamCodec.VAR_INT, ClientboundSetScorePacket::value,
            ComponentCodecs.NBT.optional(), ClientboundSetScorePacket::displayName,
            StreamCodec.VAR_INT.optional(), ClientboundSetScorePacket::numberFormat,
            ::ClientboundSetScorePacket
        )
    }
}