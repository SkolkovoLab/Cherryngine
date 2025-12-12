package ru.cherryngine.engine.core

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.network.NettyServer
import ru.cherryngine.lib.minecraft.network.protocol.packets.registry.ClientboundPacketRegistry
import ru.cherryngine.lib.minecraft.network.protocol.packets.registry.ServerboundPacketRegistry

@Factory
class MinecraftModule {
    @Singleton
    fun getClientboundPacketRegistry() = ClientboundPacketRegistry()

    @Singleton
    fun getServerboundPacketRegistry() = ServerboundPacketRegistry()

    @Singleton
    fun getNettyServer(
        clientboundPacketRegistry: ClientboundPacketRegistry,
        serverboundPacketRegistry: ServerboundPacketRegistry,
    ) = NettyServer(
        clientboundPacketRegistry,
        serverboundPacketRegistry
    )
}