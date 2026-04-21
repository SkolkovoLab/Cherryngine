package ru.cherryngine.platform.minecraft.java.events

import ru.cherryngine.platform.minecraft.java.player.MinecraftPlayer

data class PlayerConfigurationAsyncEvent(
    val player: MinecraftPlayer,
)