package ru.cherryngine.impl.demo

import io.github.dockyardmc.server.Connection
import jakarta.inject.Singleton

@Singleton
class PlayerManager {
    val map = mutableMapOf<Connection, Player>()
}