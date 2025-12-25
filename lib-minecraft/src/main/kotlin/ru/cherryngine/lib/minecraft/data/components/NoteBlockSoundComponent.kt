package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class NoteBlockSoundComponent(
    val sound: String
) : DataComponent() {

    companion object {
        val CODEC = Codec.STRING.transform(
            ::NoteBlockSoundComponent,
            NoteBlockSoundComponent::sound
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, NoteBlockSoundComponent::sound,
            ::NoteBlockSoundComponent
        )
    }
}