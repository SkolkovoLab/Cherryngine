package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton

@InstanceSingleton
class CommandDispatcher(private val sources: List<CommandSource<*>>) {
    @Suppress("UNCHECKED_CAST")
    fun pollCommands(player: Player): List<String> =
        (sources.firstOrNull { it.canHandle(player) } as? CommandSource<Player>)
            ?.pollCommands(player) ?: emptyList()
}
