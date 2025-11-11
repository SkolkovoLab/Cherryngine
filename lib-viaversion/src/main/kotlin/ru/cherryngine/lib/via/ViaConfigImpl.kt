package ru.cherryngine.lib.via

import com.viaversion.viaversion.configuration.AbstractViaConfig
import org.slf4j.LoggerFactory
import ru.cherryngine.lib.minecraft.utils.Slf4jToJulAdapter
import java.io.File
import org.slf4j.Logger as Slf4jLogger

class ViaConfigImpl : AbstractViaConfig(
    File("./via/config.yml"),
    Slf4jToJulAdapter(logger)
) {
    companion object {
        private val logger: Slf4jLogger = LoggerFactory.getLogger(ViaConfigImpl::class.java)
        private val unsupported = sequenceOf(
            BUKKIT_ONLY_OPTIONS,
            VELOCITY_ONLY_OPTIONS
        ).flatten().toList()
    }

    override fun handleConfig(config: MutableMap<String, Any>) = Unit

    override fun getUnsupportedOptions(): List<String> {
        return unsupported
    }
}