package ru.cherryngine.engine.minecraft

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.network.NettyServer

@Factory
class MinecraftModule {
    @Singleton
    fun getNettyServer() = NettyServer()
}
