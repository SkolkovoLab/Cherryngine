package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.server.Connection

data class ConnectEvent(
    val connection: Connection,
)