package cz.lukynka.prettylog

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val LOGGERS = hashMapOf<String, Logger>()

private fun getLogger(type: CustomLogType): Logger {
    val name = "Dockyard " + type.name.substringAfter(" ")
    return LOGGERS.computeIfAbsent(name) { LoggerFactory.getLogger(name) }
}

fun log(message: String, type: CustomLogType = LogType.RUNTIME) {
    val logger = getLogger(type)
    when (type) {
        LogType.TRACE -> logger.trace(message)
        LogType.DEBUG -> logger.debug(message)
        LogType.WARNING -> logger.warn(message)
        LogType.ERROR, LogType.EXCEPTION, LogType.FATAL, LogType.CRITICAL -> logger.error(message)
        else -> logger.info(message)
    }
}

fun log(exception: Exception) {
    val logger = getLogger(LogType.EXCEPTION)
    logger.error("Error", exception)
}