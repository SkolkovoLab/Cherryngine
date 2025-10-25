package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.server.Connection

@Singleton
class PlayerManager {
    val map = mutableMapOf<Connection, Player>()
}