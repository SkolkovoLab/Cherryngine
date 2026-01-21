package ru.cherryngine.lib.viaversion

import com.viaversion.viabackwards.api.ViaBackwardsPlatform
import java.io.File
import java.util.logging.Logger as JulLogger

class ViaBackwardsPlatformImpl : ViaBackwardsPlatform {
    private val julLogger = JulLogger.getLogger(ViaBackwardsPlatformImpl::class.java.name)
    override fun getLogger(): JulLogger = julLogger
    override fun disable() = Unit
    override fun getDataFolder(): File = File("./via/backwards/")
}