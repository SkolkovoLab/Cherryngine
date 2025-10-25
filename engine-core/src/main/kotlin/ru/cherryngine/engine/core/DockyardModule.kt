package ru.cherryngine.engine.core

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.DockyardServer

@Factory
class DockyardModule(
    private val engineCoreConfig: EngineCoreConfig,
) {
    @Singleton
    fun getDockyardServer(): DockyardServer {
        return DockyardServer(
            engineCoreConfig.address,
            engineCoreConfig.port
        )
    }
}