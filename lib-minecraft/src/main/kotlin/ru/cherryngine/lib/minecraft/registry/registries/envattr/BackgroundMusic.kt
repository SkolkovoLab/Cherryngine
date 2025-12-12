package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

class BackgroundMusic(
    val music: Music?,
    val creativeMusic: Music?,
    val underwaterMusic: Music?,
) {
    companion object {
        val EMPTY: BackgroundMusic = BackgroundMusic(null, null, null)
        val OVERWORLD: BackgroundMusic = BackgroundMusic(Music.GAME, Music.CREATIVE, null)

        val CODEC: Codec<BackgroundMusic> = StructCodec.of(
            "music", Music.CODEC.optional(), BackgroundMusic::music,
            "creative_music", Music.CODEC.optional(), BackgroundMusic::creativeMusic,
            "underwater_music", Music.CODEC.optional(), BackgroundMusic::underwaterMusic,
            ::BackgroundMusic
        )
    }
}