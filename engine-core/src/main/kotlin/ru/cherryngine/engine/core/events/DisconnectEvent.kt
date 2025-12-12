package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.network.Connection

data class DisconnectEvent(
    val connection: Connection,
)