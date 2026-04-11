package ru.cherryngine.engine.minecraft.events

import ru.cherryngine.engine.minecraft.player.MinecraftPlayer

data class PlayerConfigurationAsyncEvent(
    val player: MinecraftPlayer,
)