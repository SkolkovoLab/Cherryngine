package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class JukeboxSong(
    val soundEvent: String,
    val description: Component,
    val lengthInSeconds: Float,
    val comparatorOutput: Int,
) {
    companion object {
        val CODEC = StructCodec.of(
            "sound_event", Codec.STRING, JukeboxSong::soundEvent,
            "description", ComponentCodec, JukeboxSong::description,
            "length_in_seconds", Codec.FLOAT, JukeboxSong::lengthInSeconds,
            "comparator_output", Codec.INT, JukeboxSong::comparatorOutput,
            ::JukeboxSong
        )
    }
}