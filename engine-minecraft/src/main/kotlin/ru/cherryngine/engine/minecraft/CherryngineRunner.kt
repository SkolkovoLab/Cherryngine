package ru.cherryngine.engine.minecraft

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.network.NettyServer

@Singleton
class CherryngineRunner(
    private val nettyServer: NettyServer,
    private val engineCoreConfig: EngineCoreConfig,
    private val packetHandlerImpl: ConnectionHandlerImpl,
) : ApplicationEventListener<StartupEvent> {
    override fun onApplicationEvent(event: StartupEvent) {
        nettyServer.start(
            engineCoreConfig.address,
            engineCoreConfig.port,
            engineCoreConfig.mojangAuth,
            engineCoreConfig.compressionThreshold,
            packetHandlerImpl
        )
    }
}