package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.network.Connection

data class ConnectEvent(
    val connection: Connection,
)