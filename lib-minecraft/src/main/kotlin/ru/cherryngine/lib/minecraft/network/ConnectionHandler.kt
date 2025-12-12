package ru.cherryngine.lib.minecraft.network

import ru.cherryngine.lib.minecraft.network.protocol.packets.ServerboundPacket
import ru.cherryngine.lib.minecraft.network.protocol.types.GameProfile

interface ConnectionHandler {
    fun onPacket(connection: Connection, packet: ServerboundPacket) = Unit
    fun onConnect(connection: Connection) = Unit
    fun onDisconnect(connection: Connection) = Unit
    fun setGameProfile(
        connection: Connection,
        helloGameProfile: GameProfile,
        onlineGameProfile: GameProfile?,
    ): GameProfile = onlineGameProfile ?: helloGameProfile
}