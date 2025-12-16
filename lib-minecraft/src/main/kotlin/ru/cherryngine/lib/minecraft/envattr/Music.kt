package ru.cherryngine.lib.minecraft.envattr

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.registry.keys.SoundEvents

data class Music(
    val sound: Key,
    val minDelay: Int,
    val maxDelay: Int,
    val replaceCurrentMusic: Boolean,
) {
    companion object {
        val MENU: Music = Music(SoundEvents.MUSIC_MENU.key(), 20, 600, true)
        val CREATIVE: Music = Music(SoundEvents.MUSIC_CREATIVE.key(), 12000, 24000, false)
        val CREDITS: Music = Music(SoundEvents.MUSIC_CREDITS.key(), 0, 0, true)
        val END_BOSS: Music = Music(SoundEvents.MUSIC_DRAGON.key(), 0, 0, true)
        val END: Music = Music(SoundEvents.MUSIC_END.key(), 6000, 24000, true)
        val UNDER_WATER: Music = Music(SoundEvents.MUSIC_UNDER_WATER.key(), 12000, 24000, false)
        val GAME: Music = Music(SoundEvents.MUSIC_GAME.key(), 12000, 24000, false)

        val CODEC: Codec<Music> = StructCodec.of(
            "sound", Codec.KEY, Music::sound,
            "min_delay", Codec.INT, Music::minDelay,
            "max_delay", Codec.INT, Music::maxDelay,
            "replace_current_music", Codec.BOOLEAN, Music::replaceCurrentMusic,
            ::Music
        )
    }
}