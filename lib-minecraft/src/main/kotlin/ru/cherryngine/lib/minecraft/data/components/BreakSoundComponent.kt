package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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