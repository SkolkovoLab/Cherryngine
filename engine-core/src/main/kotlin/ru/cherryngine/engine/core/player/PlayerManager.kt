package ru.cherryngine.engine.core.player

import jakarta.inject.Singleton
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Singleton
class PlayerManager {
    private val playersByUUID = ConcurrentHashMap<UUID, Player>()
    private val playersByUsername = ConcurrentHashMap<String, Player>()

    fun register(player: Player) {
        playersByUUID[player.uuid] = player
        playersByUsername[player.username.lowercase()] = player
    }

    fun unregister(uuid: UUID) {
        val player = playersByUUID.remove(uuid) ?: return
        playersByUsername.remove(player.username.lowercase())
    }

    fun getPlayerNullable(uuid: UUID): Player? = playersByUUID[uuid]
    fun getPlayerNullable(username: String): Player? = playersByUsername[username.lowercase()]
    fun getPlayer(uuid: UUID): Player = playersByUUID[uuid] ?: error("Player $uuid not found")
    fun onlinePlayers(): Collection<Player> = playersByUUID.values
}
