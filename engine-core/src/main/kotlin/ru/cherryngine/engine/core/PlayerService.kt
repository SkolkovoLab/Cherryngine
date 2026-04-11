package ru.cherryngine.engine.core

import jakarta.inject.Singleton

@Singleton
class PlayerService(private val handlers: List<PlayerServiceHandler>) {
    fun onPlayerJoin(player: Player) {
        handlers.forEach { if (it.canHandle(player)) it.onPlayerJoin(player) }
    }

    fun onPlayerLeave(player: Player) {
        handlers.forEach { if (it.canHandle(player)) it.onPlayerLeave(player) }
    }
}
