package ru.cherryngine.platform.minecraft.java.events

import ru.cherryngine.platform.minecraft.java.network.Connection

data class DisconnectEvent(
    val connection: Connection,
)