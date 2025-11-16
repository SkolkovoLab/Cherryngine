package ru.cherryngine.integration.grim.command

import ac.grim.grimac.platform.api.sender.SenderFactory
import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.core.commandmanager.CommandSender
import java.util.*

@Singleton
class SenderFactoryImpl : SenderFactory<CommandSender>() {
    override fun getUniqueId(sender: CommandSender): UUID {
        sender as Player
        return sender.connection.gameProfile.uuid
    }

    override fun getName(sender: CommandSender): String {
        sender as Player
        return sender.connection.gameProfile.username
    }

    override fun sendMessage(
        sender: CommandSender,
        message: String,
    ) {
        sender.sendMessage(message)
    }

    override fun sendMessage(
        sender: CommandSender,
        message: Component,
    ) {
        sender.sendMessage(message)
    }

    override fun hasPermission(
        sender: CommandSender,
        node: String,
    ): Boolean {
        return true
    }

    override fun hasPermission(
        sender: CommandSender?,
        node: String?,
        defaultIfUnset: Boolean,
    ): Boolean {
        return true
    }

    override fun performCommand(
        sender: CommandSender,
        command: String,
    ) {
        TODO("Not yet implemented")
    }

    override fun isConsole(sender: CommandSender): Boolean {
        return false
    }

    override fun isPlayer(sender: CommandSender): Boolean {
        return sender is Player
    }
}