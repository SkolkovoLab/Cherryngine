package ru.cherryngine.engine.core

import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class WorldService(private val handlers: List<WorldServiceHandler>) {
    fun setPlayerContext(uuid: UUID, contextIDs: Set<String>, player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.setPlayerContext(uuid, contextIDs)
    }

    fun onPlayerJoin(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerJoin(player)
    }

    fun onPlayerLeave(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerLeave(player)
    }
}
