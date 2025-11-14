package ru.cherryngine.engine.core.commandmanager

import org.incendo.cloud.annotations.exception.ExceptionHandler
import org.slf4j.LoggerFactory

@CloudCommand
class CommandExceptionHandler {
    private val logger = LoggerFactory.getLogger(CommandExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    fun handleException(sender: CommandSender, exception: Exception) {
        logger.error("Unhandled player exception. CommandSender: $sender", exception)
    }
}