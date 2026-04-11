package ru.cherryngine.engine.core

import jakarta.inject.Singleton

@Singleton
class ModelService(private val handlers: List<ModelServiceHandler>) {
    fun onPlayerJoin(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerJoin(player)
    }

    fun onPlayerLeave(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerLeave(player)
    }
}
