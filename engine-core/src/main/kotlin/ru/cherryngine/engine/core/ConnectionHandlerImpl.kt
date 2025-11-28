package ru.cherryngine.engine.core

import io.micronaut.context.event.ApplicationEventPublisher
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.events.ConnectEvent
import ru.cherryngine.engine.core.events.DisconnectEvent
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.engine.core.events.SetGameProfileEvent
import ru.cherryngine.lib.minecraft.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.protocol.types.GameProfile
import ru.cherryngine.lib.minecraft.server.Connection
import ru.cherryngine.lib.minecraft.server.ConnectionHandler

@Singleton
class ConnectionHandlerImpl(
    val packetEventPublisher: ApplicationEventPublisher<PacketEvent>,
    val connectEventPublisher: ApplicationEventPublisher<ConnectEvent>,
    val disconnectEventPublisher: ApplicationEventPublisher<DisconnectEvent>,
    val setGameProfileEventPublisher: ApplicationEventPublisher<SetGameProfileEvent>,
) : ConnectionHandler {
    override fun onPacket(connection: Connection, packet: ServerboundPacket) {
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
        helloGameProfile: GameProfile,
        onlineGameProfile: GameProfile?,
    ): GameProfile {
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