package ru.cherryngine.engine.mcprotocollib

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.services.WorldServiceHandler
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Singleton
class McProtocolLibWorldServiceHandler(
    private val playerManager: PlayerManager,
) : WorldServiceHandler {
    private val playerContexts = ConcurrentHashMap<UUID, Set<String>>()

    override fun canHandle(player: Player) = player is McProtocolLibPlayer

    override fun onPlayerJoin(player: Player) {
        playerContexts[player.uuid] = emptySet()
    }

    override fun onPlayerLeave(player: Player) {
        playerContexts.remove(player.uuid)
    }

    override fun setPlayerContext(uuid: UUID, contextIDs: Set<String>) {
        val prev = playerContexts[uuid]
        if (prev == contextIDs) return
        playerContexts[uuid] = contextIDs
        val player = playerManager.getPlayerNullable(uuid) as? McProtocolLibPlayer ?: return
        player.sentChunksBase = null
        player.sentChunks.clear()
    }

    fun getContextsForPlayer(uuid: UUID): Set<String> = playerContexts[uuid] ?: emptySet()
}
