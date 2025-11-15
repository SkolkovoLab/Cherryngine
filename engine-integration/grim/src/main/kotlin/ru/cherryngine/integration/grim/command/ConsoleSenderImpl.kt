package ru.cherryngine.integration.grim.command

import ac.grim.grimac.platform.api.player.PlatformPlayer
import ac.grim.grimac.platform.api.sender.Sender
import net.kyori.adventure.text.Component
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.utils.plainString
import java.util.*

object ConsoleSenderImpl : Sender {
    private val logger = LoggerFactory.getLogger(ConsoleSenderImpl::class.java)

    override fun getName(): String {
        return Sender.CONSOLE_NAME
    }

    override fun getUniqueId(): UUID {
        return Sender.CONSOLE_UUID
    }

    override fun sendMessage(message: String?) {
        logger.info(message)
    }

    override fun sendMessage(message: Component?) {
        logger.info(message?.plainString())
    }

    override fun hasPermission(permission: String?): Boolean {
        return true
    }

    override fun hasPermission(permission: String?, defaultIfUnset: Boolean): Boolean {
        return true
    }

    override fun performCommand(commandLine: String?) {

    }

    override fun isConsole(): Boolean {
        return true
    }

    override fun isPlayer(): Boolean {
        return false
    }

    override fun getNativeSender(): Any {
        TODO("Not yet implemented")
    }

    override fun getPlatformPlayer(): PlatformPlayer {
        TODO("Not yet implemented")
    }
}