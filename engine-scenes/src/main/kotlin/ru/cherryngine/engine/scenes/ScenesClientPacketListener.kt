package ru.cherryngine.engine.scenes

import jakarta.inject.Singleton
import net.minestom.server.network.packet.client.ClientPacket
import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.core.server.ClientPacketListener
import ru.cherryngine.engine.scenes.event.impl.ClientPacketEvent
import ru.cherryngine.engine.scenes.event.impl.DisconnectEvent

@Singleton
class ScenesClientPacketListener(
    val sceneManager: SceneManager,
) : ClientPacketListener {
    override fun onPacketReceived(
        clientConnection: ClientConnection,
        packet: ClientPacket,
    ) {
        sceneManager.fireGlobalEvent(ClientPacketEvent(clientConnection, packet))
    }

    override fun onDisconnect(clientConnection: ClientConnection) {
        sceneManager.fireGlobalEvent(DisconnectEvent(clientConnection))
    }
}