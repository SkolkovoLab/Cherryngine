package ru.cherryngine.engine.core.commandmanager

import net.kyori.adventure.text.Component

interface CommandSender {
    fun sendMessage(message: Component)
    fun sendMessage(message: String) = sendMessage(Component.text(message))
}