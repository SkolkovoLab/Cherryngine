package ru.cherryngine.engine.minecraft.events

import ru.cherryngine.lib.minecraft.network.Connection

data class ConnectEvent(
    val connection: Connection,
)