package ru.cherryngine.engine.core.events

import ru.cherryngine.lib.minecraft.server.Connection

data class DisconnectEvent(
    val connection: Connection,
)