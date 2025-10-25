package io.github.dockyardmc.protocol.packets.play.serverbound

import io.github.dockyardmc.protocol.packets.ServerboundPacket
import io.github.dockyardmc.tide.stream.StreamCodec

data class ServerboundPlayerAbilitiesPacket(
    val abilities: Abilities
) : ServerboundPacket {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Abilities.STREAM_CODEC, ServerboundPlayerAbilitiesPacket::abilities,
            ::ServerboundPlayerAbilitiesPacket
        )
    }

    data class Abilities(
        val flying: Boolean
    ) {
        companion object {
            val STREAM_CODEC = StreamCodec.byteFlags(
                0x02, Abilities::flying,
                ::Abilities
            )
        }
    }
}