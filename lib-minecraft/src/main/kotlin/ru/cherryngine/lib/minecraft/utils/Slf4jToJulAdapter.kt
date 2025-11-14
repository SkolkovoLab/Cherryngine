package ru.cherryngine.lib.minecraft.utils

import java.util.logging.Level
import org.slf4j.Logger as Slf4jLogger
import java.util.logging.Logger as JulLogger

class Slf4jToJulAdapter(
    private val slf4j: Slf4jLogger,
) : JulLogger(slf4j.name, null) {
    companion object {
        fun log(slf4j: Slf4jLogger, level: Level, msg: String?) {
            when (level) {
                Level.SEVERE -> slf4j.error(msg)
                Level.WARNING -> slf4j.warn(msg)
                Level.INFO -> slf4j.info(msg)
                Level.CONFIG -> slf4j.info(msg)
                Level.FINE -> slf4j.debug(msg)
                Level.FINER -> slf4j.debug(msg)
                Level.FINEST -> slf4j.trace(msg)
            }
        }
    }

    override fun log(level: Level, msg: String?) = log(slf4j, level, msg)
}