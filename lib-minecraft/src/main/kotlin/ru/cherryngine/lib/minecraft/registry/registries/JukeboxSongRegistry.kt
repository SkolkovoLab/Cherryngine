package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.JukeboxSong

object JukeboxSongRegistry : DataDrivenRegistry<JukeboxSong>(
    "minecraft:jukebox_song",
    "registry/jukebox_song_registry.json.gz",
    JukeboxSong.serializer()
)
