package ru.cherryngine.engine.core

interface ModelServiceHandler {
    fun canHandle(player: Player): Boolean
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
}
