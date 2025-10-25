package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundSetExperiencePacket(
    val experienceBar: Float,
    val level: Int,
    val totalExperience: Int
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, ClientboundSetExperiencePacket::experienceBar,
            StreamCodec.VAR_INT, ClientboundSetExperiencePacket::level,
            StreamCodec.VAR_INT, ClientboundSetExperiencePacket::totalExperience,
            ::ClientboundSetExperiencePacket
        )
    }
}