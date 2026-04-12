package ru.cherryngine.engine.core.services

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.Player

@Singleton
class WorldService(private val handlers: List<WorldServiceHandler>) {
    fun setPlayerContext(contextIDs: Set<String>, player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.setPlayerContext(player.uuid, contextIDs)
    }

    fun onPlayerJoin(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerJoin(player)
    }

    fun onPlayerLeave(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerLeave(player)
    }
}
