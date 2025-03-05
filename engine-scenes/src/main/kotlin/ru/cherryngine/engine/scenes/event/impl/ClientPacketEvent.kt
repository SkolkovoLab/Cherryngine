package ru.cherryngine.engine.scenes.event.impl

import net.minestom.server.network.packet.client.ClientPacket
import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.scenes.event.Event

data class ClientPacketEvent(
    val clientConnection: ClientConnection,
    val packet: ClientPacket
) : Event