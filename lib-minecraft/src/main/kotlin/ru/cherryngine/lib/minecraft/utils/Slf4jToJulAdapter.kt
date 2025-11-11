package ru.cherryngine.lib.minecraft.utils

import org.slf4j.Logger as Slf4jLogger
import java.util.logging.Logger as JulLogger

class Slf4jToJulAdapter(
    private val slf4j: Slf4jLogger,
) : JulLogger(slf4j.name, null) {
    override fun info(msg: String?) = slf4j.info(msg)
    override fun warning(msg: String?) = slf4j.warn(msg)
    override fun severe(msg: String?) = slf4j.error(msg)
    override fun fine(msg: String?) = slf4j.debug(msg)
    override fun finer(msg: String?) = slf4j.debug(msg)
    override fun finest(msg: String?) = slf4j.trace(msg)
}