package ru.cherryngine.engine.minecraft.commandmanager

import net.kyori.adventure.text.Component

interface CommandSender {
    fun sendMessage(message: Component)
    fun sendMessage(message: String) = sendMessage(Component.text(message))
}