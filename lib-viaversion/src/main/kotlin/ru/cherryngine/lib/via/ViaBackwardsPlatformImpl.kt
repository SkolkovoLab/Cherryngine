package ru.cherryngine.lib.via

import com.viaversion.viabackwards.api.ViaBackwardsPlatform
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.utils.Slf4jToJulAdapter
import java.io.File
import java.util.logging.Logger as JulLogger

class ViaBackwardsPlatformImpl : ViaBackwardsPlatform {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val julLogger = Slf4jToJulAdapter(logger)
    override fun getLogger(): JulLogger = julLogger

    override fun disable() = Unit

    override fun getDataFolder(): File = File("./via/backwards/")
}