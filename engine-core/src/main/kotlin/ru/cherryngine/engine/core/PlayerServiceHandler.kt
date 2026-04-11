package ru.cherryngine.engine.core

interface PlayerServiceHandler {
    fun canHandle(player: Player): Boolean
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
}
