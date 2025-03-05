package ru.cherryngine.engine.core.server

import net.minestom.server.network.packet.client.ClientPacket

interface ClientPacketListener {
    fun onPacketReceived(clientConnection: ClientConnection, packet: ClientPacket)
    fun onDisconnect(clientConnection: ClientConnection)
}