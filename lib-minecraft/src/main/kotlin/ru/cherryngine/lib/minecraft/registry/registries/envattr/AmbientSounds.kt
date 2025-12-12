package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.network.protocol.types.SoundEvent

data class AmbientSounds(
    val loop: SoundEvent?,
    val mood: Mood?,
    val additions: List<Additions>,
) {
    data class Mood(
        val sound: SoundEvent,
        val tickDelay: Int,
        val blockSearchExtent: Int,
        val offset: Double,
    ) {
        companion object {
            val CODEC: Codec<Mood> = StructCodec.of(
                "sound", SoundEvent.CODEC, Mood::sound,
                "tick_delay", Codec.INT, Mood::tickDelay,
                "block_search_extent", Codec.INT, Mood::blockSearchExtent,
                "offset", Codec.DOUBLE, Mood::offset,
                ::Mood
            )
        }
    }

    data class Additions(
        val sound: SoundEvent,
        val tickChance: Double,
    ) {
        companion object {
            val CODEC: Codec<Additions> = StructCodec.of(
                "sound", SoundEvent.CODEC, Additions::sound,
                "tick_chance", Codec.DOUBLE, Additions::tickChance,
                ::Additions
            )
        }
    }

    companion object {
        val EMPTY: AmbientSounds = AmbientSounds(null, null, listOf())

        val CODEC: Codec<AmbientSounds> = StructCodec.of(
            "loop", SoundEvent.CODEC.optional(), AmbientSounds::loop,
            "mood", Mood.CODEC.optional(), AmbientSounds::mood,
            "additions", Additions.CODEC.listOrSingle().default(listOf()), AmbientSounds::additions,
            ::AmbientSounds
        )
    }
}