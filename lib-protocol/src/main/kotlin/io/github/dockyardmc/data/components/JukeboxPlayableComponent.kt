package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.registry.registries.JukeboxSong
import io.github.dockyardmc.registry.registries.JukeboxSongRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

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