package ru.cherryngine.engine.core

import io.micronaut.context.event.ApplicationEventPublisher
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.events.ConnectEvent
import ru.cherryngine.engine.core.events.DisconnectEvent
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.engine.core.events.SetGameProfileEvent
import ru.cherryngine.lib.minecraft.network.Connection
import ru.cherryngine.lib.minecraft.network.ConnectionHandler

@Singleton
class ConnectionHandlerImpl(
    val packetEventPublisher: ApplicationEventPublisher<PacketEvent>,
    val connectEventPublisher: ApplicationEventPublisher<ConnectEvent>,
    val disconnectEventPublisher: ApplicationEventPublisher<DisconnectEvent>,
    val setGameProfileEventPublisher: ApplicationEventPublisher<SetGameProfileEvent>,
) : ConnectionHandler {
    override fun onPacket(connection: Connection, packet: ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket) {
        packetEventPublisher.publishEvent(PacketEvent(connection, packet))
    }

    override fun onConnect(connection: Connection) {
        connectEventPublisher.publishEvent(ConnectEvent(connection))
    }

    override fun onDisconnect(connection: Connection) {
        disconnectEventPublisher.publishEvent(DisconnectEvent(connection))
    }

    override fun setGameProfile(
        connection: Connection,
        helloGameProfile: ru.cherryngine.lib.minecraft.network.protocol.types.GameProfile,
        onlineGameProfile: ru.cherryngine.lib.minecraft.network.protocol.types.GameProfile?,
    ): ru.cherryngine.lib.minecraft.network.protocol.types.GameProfile {
        val event = SetGameProfileEvent(
            connection,
            helloGameProfile,
            onlineGameProfile,
            onlineGameProfile ?: helloGameProfile
        )
        setGameProfileEventPublisher.publishEvent(event)
        return event.gameProfile
    }
}