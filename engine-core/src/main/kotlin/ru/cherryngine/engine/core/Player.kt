package ru.cherryngine.engine.core

import net.kyori.adventure.text.Component
import java.util.UUID

interface Player {
    val uuid: UUID
    val username: String
    fun sendMessage(message: Component)
}
