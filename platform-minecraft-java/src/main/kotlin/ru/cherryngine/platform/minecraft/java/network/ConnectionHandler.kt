package ru.cherryngine.platform.minecraft.java.network

import net.minestom.server.network.packet.client.ClientPacket
import net.minestom.server.network.player.GameProfile

interface ConnectionHandler {
    fun onPacket(connection: Connection, packet: ClientPacket) = Unit
    fun onConnect(connection: Connection) = Unit
    fun onDisconnect(connection: Connection) = Unit
    fun setGameProfile(
        connection: Connection,
        helloGameProfile: GameProfile,
        onlineGameProfile: GameProfile?,
    ): GameProfile = onlineGameProfile ?: helloGameProfile
}
