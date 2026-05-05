package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.platform.PlatformHandler

/**
 * Источник tab-complete запросов игрока.
 */
interface SuggestionSource<in P : Player> : PlatformHandler<Player> {
    fun pollSuggestions(player: P): List<SuggestionRequest>
}

data class SuggestionRequest(val transactionId: Int, val input: String)
