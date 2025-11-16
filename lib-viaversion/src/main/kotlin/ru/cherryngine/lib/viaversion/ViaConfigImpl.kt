package ru.cherryngine.lib.viaversion

import com.viaversion.viaversion.configuration.AbstractViaConfig
import java.io.File
import java.util.logging.Logger

class ViaConfigImpl(
    file: File = File("./viaversion/config.yml"),
) : AbstractViaConfig(
    file,
    logger
) {
    companion object {
        private val logger = Logger.getLogger(ViaConfigImpl::class.java.name)
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