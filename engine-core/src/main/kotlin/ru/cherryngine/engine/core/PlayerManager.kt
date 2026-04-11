package ru.cherryngine.engine.core

import jakarta.inject.Singleton
import kotlinx.coroutines.channels.Channel
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Singleton
class PlayerManager {
    val playerJoinChannel = Channel<UUID>(Channel.UNLIMITED)
    val playerLeaveChannel = Channel<UUID>(Channel.UNLIMITED)

    private val playersByUUID = ConcurrentHashMap<UUID, Player>()
    private val playersByUsername = ConcurrentHashMap<String, Player>()

    fun register(player: Player) {
        playersByUUID[player.uuid] = player
        playersByUsername[player.username.lowercase()] = player
        playerJoinChannel.trySend(player.uuid)
    }

    fun unregister(uuid: UUID) {
        val player = playersByUUID.remove(uuid)
        if (player != null) {
            playersByUsername.remove(player.username.lowercase())
        }
        playerLeaveChannel.trySend(uuid)
    }

    fun getPlayerNullable(uuid: UUID): Player? = playersByUUID[uuid]

    fun getPlayerNullable(username: String): Player? = playersByUsername[username.lowercase()]

    fun getPlayer(uuid: UUID): Player = playersByUUID[uuid] ?: error("Player $uuid not found")

    fun onlinePlayers(): Collection<Player> = playersByUUID.values
}
