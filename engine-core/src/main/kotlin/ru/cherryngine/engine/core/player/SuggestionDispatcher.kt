package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton

@InstanceSingleton
class SuggestionDispatcher(private val sources: List<SuggestionSource<*>>) {
    @Suppress("UNCHECKED_CAST")
    fun pollSuggestions(player: Player): List<SuggestionRequest> =
        (sources.firstOrNull { it.canHandle(player) } as? SuggestionSource<Player>)
            ?.pollSuggestions(player) ?: emptyList()
}
