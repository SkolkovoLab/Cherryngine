package ru.cherryngine.engine.minecraft.events

import ru.cherryngine.engine.minecraft.player.MinecraftPlayer

data class PlayerCreatedEvent(
    val player: MinecraftPlayer,
)