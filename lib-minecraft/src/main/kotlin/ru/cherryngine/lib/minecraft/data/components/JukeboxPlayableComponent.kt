package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.JukeboxSong

class JukeboxPlayableComponent(
    val jukeboxSong: JukeboxSong,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return unsupported(this)
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.jukeboxSong.streamCodec, JukeboxPlayableComponent::jukeboxSong,
            ::JukeboxPlayableComponent
        )
    }
}