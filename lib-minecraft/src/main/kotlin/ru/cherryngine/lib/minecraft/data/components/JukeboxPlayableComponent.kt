package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.registry.registries.JukeboxSong
import ru.cherryngine.lib.minecraft.registry.registries.JukeboxSongRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

class JukeboxPlayableComponent(
    val jukeboxSong: JukeboxSong
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(JukeboxSongRegistry), JukeboxPlayableComponent::jukeboxSong,
            ::JukeboxPlayableComponent
        )
    }
}