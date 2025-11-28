package ru.cherryngine.engine.core

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ClientboundPacketRegistry
import ru.cherryngine.lib.minecraft.protocol.packets.registry.ServerboundPacketRegistry
import ru.cherryngine.lib.minecraft.server.NettyServer

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