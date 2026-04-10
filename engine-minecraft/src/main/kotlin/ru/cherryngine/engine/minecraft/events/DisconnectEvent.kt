package ru.cherryngine.engine.minecraft.events

import ru.cherryngine.lib.minecraft.network.Connection

data class DisconnectEvent(
    val connection: Connection,
)