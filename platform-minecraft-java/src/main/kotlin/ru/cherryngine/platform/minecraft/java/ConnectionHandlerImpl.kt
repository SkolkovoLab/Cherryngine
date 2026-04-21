package ru.cherryngine.platform.minecraft.java

import io.micronaut.context.event.ApplicationEventPublisher
import jakarta.inject.Singleton
import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.player.GameProfile
import ru.cherryngine.platform.minecraft.java.events.ConnectEvent
import ru.cherryngine.platform.minecraft.java.events.DisconnectEvent
import ru.cherryngine.platform.minecraft.java.events.PacketEvent
import ru.cherryngine.platform.minecraft.java.events.SetGameProfileEvent
import ru.cherryngine.platform.minecraft.java.network.Connection
import ru.cherryngine.platform.minecraft.java.network.ConnectionHandler

@Singleton
class ConnectionHandlerImpl(
    val packetEventPublisher: ApplicationEventPublisher<PacketEvent>,
    val connectEventPublisher: ApplicationEventPublisher<ConnectEvent>,
    val disconnectEventPublisher: ApplicationEventPublisher<DisconnectEvent>,
    val setGameProfileEventPublisher: ApplicationEventPublisher<SetGameProfileEvent>,
) : ConnectionHandler {
    override fun onPacket(connection: Connection, packet: ClientPacket) {
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
