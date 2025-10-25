package ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound

import ru.cherryngine.lib.minecraft.protocol.packets.ClientboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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