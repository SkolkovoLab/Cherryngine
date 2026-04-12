package ru.cherryngine.engine.core.services

import ru.cherryngine.engine.core.player.Player

interface PlayerServiceHandler {
    fun canHandle(player: Player): Boolean
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
}
