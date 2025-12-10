package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.JukeboxSong

object JukeboxSongRegistry : DataDrivenRegistry<JukeboxSong>(
    "minecraft:jukebox_song",
    "jukebox_song.json",
    JukeboxSong.serializer()
)
