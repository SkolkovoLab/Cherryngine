package ru.cherryngine.engine.core

import java.util.UUID

interface WorldServiceHandler {
    fun canHandle(player: Player): Boolean
    fun setPlayerContext(uuid: UUID, contextIDs: Set<String>)
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
}
