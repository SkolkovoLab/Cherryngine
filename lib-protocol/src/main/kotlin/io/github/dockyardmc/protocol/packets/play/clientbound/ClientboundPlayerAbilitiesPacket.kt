package io.github.dockyardmc.protocol.packets.play.clientbound

import io.github.dockyardmc.protocol.packets.ClientboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ClientboundPlayerAbilitiesPacket(
    val flags: Flags,
    val flyingSpeed: Float = 0.05f,
    val fovModifier: Float = 0.1f
) : ClientboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Flags.STREAM_CODEC, ClientboundPlayerAbilitiesPacket::flags,
            StreamCodec.FLOAT, ClientboundPlayerAbilitiesPacket::flyingSpeed,
            StreamCodec.FLOAT, ClientboundPlayerAbilitiesPacket::fovModifier,
            ::ClientboundPlayerAbilitiesPacket
        )
    }

    data class Flags(
        val isInvulnerable: Boolean,
        val isFlying: Boolean,
        val allowFlying: Boolean,
        val instantBreak: Boolean,
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.byteFlags(
                0x01, Flags::isInvulnerable,
                0x02, Flags::isFlying,
                0x04, Flags::allowFlying,
                0x08, Flags::instantBreak,
                ::Flags
            )
        }
    }
}