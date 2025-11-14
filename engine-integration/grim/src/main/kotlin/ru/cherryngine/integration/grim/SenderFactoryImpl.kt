package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.sender.SenderFactory
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.server.Connection
import java.util.*

class SenderFactoryImpl : SenderFactory<Connection>() {
    override fun getUniqueId(sender: Connection?): UUID {
        TODO("Not yet implemented")
    }

    override fun getName(sender: Connection?): String {
        TODO("Not yet implemented")
    }

    override fun sendMessage(sender: Connection?, message: String?) {
        TODO("Not yet implemented")
    }

    override fun sendMessage(
        sender: Connection?,
        message: Component?,
    ) {
        TODO("Not yet implemented")
    }

    override fun hasPermission(
        sender: Connection?,
        node: String?,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(
        sender: Connection?,
        node: String?,
        defaultIfUnset: Boolean,
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun performCommand(sender: Connection?, command: String?) {
        TODO("Not yet implemented")
    }

    override fun isConsole(sender: Connection?): Boolean {
        TODO("Not yet implemented")
    }

    override fun isPlayer(sender: Connection?): Boolean {
        TODO("Not yet implemented")
    }
}