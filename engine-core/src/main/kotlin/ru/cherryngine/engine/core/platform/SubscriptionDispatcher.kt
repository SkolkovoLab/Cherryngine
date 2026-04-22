package ru.cherryngine.engine.core.platform

import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.engine.core.player.PlayerManager
import java.util.UUID

class SubscriptionDispatcher(
    private val renderers: List<EntityRenderer<*>>,
    private val playerManager: PlayerManager,
) {
    private val subscribers = HashMap<UUID, MutableSet<UUID>>()

    fun onAdd(id: UUID) = renderers.forEach { it.onAdd(id) }

    fun onRemove(id: UUID) {
        subscribers.remove(id)?.forEach { playerId ->
            val player = playerManager.getPlayerNullable(playerId) ?: return@forEach
            dispatchHide(id, player)
        }
        renderers.forEach { it.onRemove(id) }
    }

    fun syncSubscribers(id: UUID, viewContextIDs: Set<String>) {
        val target = HashSet<UUID>()
        for (player in playerManager.onlinePlayers()) {
            val playerCtx = player.viewContextIDs
            if (viewContextIDs.isEmpty() || viewContextIDs.any { it in playerCtx }) {
                target.add(player.uuid)
            }
        }
        val prev = subscribers.getOrPut(id) { HashSet() }
        val toHide = prev - target
        val toShow = target - prev
        for (playerId in toHide) {
            val player = playerManager.getPlayerNullable(playerId) ?: continue
            dispatchHide(id, player)
        }
        for (playerId in toShow) {
            val player = playerManager.getPlayerNullable(playerId) ?: continue
            dispatchShow(id, player)
        }
        prev.removeAll(toHide)
        prev.addAll(toShow)
    }

    @Suppress("UNCHECKED_CAST")
    private fun dispatchShow(id: UUID, player: Player) =
        renderers.filter { it.canHandle(player) }
            .forEach { (it as EntityRenderer<Player>).show(id, player) }

    @Suppress("UNCHECKED_CAST")
    private fun dispatchHide(id: UUID, player: Player) =
        renderers.filter { it.canHandle(player) }
            .forEach { (it as EntityRenderer<Player>).hide(id, player) }
}
