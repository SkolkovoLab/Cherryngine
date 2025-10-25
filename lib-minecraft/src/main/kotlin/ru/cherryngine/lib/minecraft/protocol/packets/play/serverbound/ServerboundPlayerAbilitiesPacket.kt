package ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound

import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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