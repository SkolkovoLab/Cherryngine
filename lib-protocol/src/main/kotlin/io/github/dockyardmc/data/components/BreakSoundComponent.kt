package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.protocol.types.SoundEvent
import io.github.dockyardmc.tide.stream.StreamCodec

data class BreakSoundComponent(
    val sound: SoundEvent
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(sound.hashStruct().getHashed())
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            SoundEvent.STREAM_CODEC, BreakSoundComponent::sound,
            ::BreakSoundComponent
        )
    }
}