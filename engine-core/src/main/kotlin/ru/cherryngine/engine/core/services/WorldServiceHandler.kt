package ru.cherryngine.engine.core.services

import ru.cherryngine.engine.core.player.Player
import java.util.*

interface WorldServiceHandler {
    fun canHandle(player: Player): Boolean
    fun setPlayerContext(uuid: UUID, contextIDs: Set<String>)
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
}
