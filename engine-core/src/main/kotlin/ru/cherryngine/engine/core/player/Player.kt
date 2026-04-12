package ru.cherryngine.engine.core.player

import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.commandmanager.CommandSender
import java.util.*

interface Player : CommandSender {
    val uuid: UUID
    val username: String
    override fun sendMessage(message: Component)
}
