package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.keys.Sounds
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec

data class Music(
    val sound: Key,
    val minDelay: Int,
    val maxDelay: Int,
    val replaceCurrentMusic: Boolean,
) {
    companion object {
        val MENU: Music = Music(Sounds.MUSIC_MENU, 20, 600, true)
        val CREATIVE: Music = Music(Sounds.MUSIC_CREATIVE, 12000, 24000, false)
        val CREDITS: Music = Music(Sounds.MUSIC_CREDITS, 0, 0, true)
        val END_BOSS: Music = Music(Sounds.MUSIC_DRAGON, 0, 0, true)
        val END: Music = Music(Sounds.MUSIC_END, 6000, 24000, true)
        val UNDER_WATER: Music = Music(Sounds.MUSIC_UNDER_WATER, 12000, 24000, false)
        val GAME: Music = Music(Sounds.MUSIC_GAME, 12000, 24000, false)

        val CODEC: Codec<Music> = StructCodec.of(
            "sound", Codec.KEY, Music::sound,
            "min_delay", Codec.INT, Music::minDelay,
            "max_delay", Codec.INT, Music::maxDelay,
            "replace_current_music", Codec.BOOLEAN, Music::replaceCurrentMusic,
            ::Music
        )
    }
}