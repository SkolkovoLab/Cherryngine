package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.types.SoundEvent

data class BreakSoundComponent(
    val sound: SoundEvent
) : DataComponent() {
    companion object {
        val CODEC = SoundEvent.CODEC.transform(
            ::BreakSoundComponent,
            BreakSoundComponent::sound
        )
        val STREAM_CODEC = StreamCodec.of(
            SoundEvent.STREAM_CODEC, BreakSoundComponent::sound,
            ::BreakSoundComponent
        )
    }
}