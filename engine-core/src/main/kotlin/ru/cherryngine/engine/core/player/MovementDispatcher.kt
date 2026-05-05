package ru.cherryngine.engine.core.player

import ru.cherryngine.engine.core.instance.InstanceSingleton

@InstanceSingleton
class MovementDispatcher(private val sources: List<MovementSource<*>>) {
    @Suppress("UNCHECKED_CAST")
    fun pollMovement(player: Player): MovementSnapshot? =
        (sources.firstOrNull { it.canHandle(player) } as? MovementSource<Player>)
            ?.pollMovement(player)
}
