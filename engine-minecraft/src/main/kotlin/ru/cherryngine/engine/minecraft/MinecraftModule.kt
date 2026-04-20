package ru.cherryngine.engine.minecraft

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import net.minestom.server.registry.Registries
import ru.cherryngine.lib.minecraft.network.NettyServer
import ru.cherryngine.lib.minecraft.registry.CherryngineRegistries

@Factory
class MinecraftModule {
    @Singleton
    fun getRegistries(): Registries = CherryngineRegistries()

    @Singleton
    fun getNettyServer(registries: Registries) = NettyServer(registries)
}
