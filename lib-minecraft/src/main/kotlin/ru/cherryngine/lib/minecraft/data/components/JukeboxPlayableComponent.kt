package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.JukeboxSong

class JukeboxPlayableComponent(
    val jukeboxSong: JukeboxSong,
) : DataComponent() {

    companion object {
        val CODEC = Registries.jukeboxSong.keyCodec.transform(
            ::JukeboxPlayableComponent,
            JukeboxPlayableComponent::jukeboxSong
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.jukeboxSong.streamCodec, JukeboxPlayableComponent::jukeboxSong,
            ::JukeboxPlayableComponent
        )
    }
}