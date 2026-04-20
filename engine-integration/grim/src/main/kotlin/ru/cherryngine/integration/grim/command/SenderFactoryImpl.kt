package ru.cherryngine.integration.grim.command

import ac.grim.grimac.platform.api.sender.SenderFactory
import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import java.util.UUID

@Singleton
class SenderFactoryImpl : SenderFactory<CommandSender>() {
    override fun getUniqueId(sender: CommandSender): UUID {
        sender as MinecraftPlayer
        return sender.connection.gameProfile.uuid()
    }

    override fun getName(sender: CommandSender): String {
        sender as MinecraftPlayer
        return sender.connection.gameProfile.name()
    }

    override fun sendMessage(sender: CommandSender, message: String) {
        sender.sendMessage(message)
    }

    override fun sendMessage(sender: CommandSender, message: Component) {
        sender.sendMessage(message)
    }

    override fun hasPermission(sender: CommandSender, node: String): Boolean = true

    override fun hasPermission(sender: CommandSender?, node: String?, defaultIfUnset: Boolean): Boolean = true

    override fun performCommand(sender: CommandSender, command: String) {
        TODO("Not yet implemented")
    }

    override fun isConsole(sender: CommandSender): Boolean = false

    override fun isPlayer(sender: CommandSender): Boolean = sender is MinecraftPlayer
}
