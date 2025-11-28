package ru.cherryngine.engine.core.events

import ru.cherryngine.engine.core.player.Player

data class PlayerConfigurationAsyncEvent(
    val player: Player,
)