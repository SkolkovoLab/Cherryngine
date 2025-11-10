package ru.cherryngine.engine.core

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.MinecraftServer

@Factory
class MinecraftModule(
    private val engineCoreConfig: EngineCoreConfig,
) {
    @Singleton
    fun getDockyardServer(): MinecraftServer {
        return MinecraftServer(
            engineCoreConfig.address,
            engineCoreConfig.port
        )
    }
}