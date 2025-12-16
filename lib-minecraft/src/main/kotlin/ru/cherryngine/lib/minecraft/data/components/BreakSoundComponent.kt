package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.types.SoundEvent

data class BreakSoundComponent(
    val sound: SoundEvent
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(SoundEvent.CODEC.encode(CRC32CTranscoder, sound))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            SoundEvent.STREAM_CODEC, BreakSoundComponent::sound,
            ::BreakSoundComponent
        )
    }
}