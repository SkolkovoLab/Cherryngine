package ru.cherryngine.engine.minecraft.events

import ru.cherryngine.engine.minecraft.player.Player

data class PlayerCreatedEvent(
    val player: Player,
)