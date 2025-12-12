package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.JukeboxSong

object JukeboxSongRegistry : KtJsonDataDrivenRegistry<JukeboxSong>(
    "minecraft:jukebox_song",
    "jukebox_song.json",
    JukeboxSong.serializer()
)
