package ru.cherryngine.engine.core.commandmanager

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.Player
import java.util.concurrent.CompletableFuture

@Singleton
class CommandService(private val handlers: List<CommandServiceHandler>) {
    fun onPlayerJoin(player: Player) {
        handlers.firstOrNull { it.canHandle(player) }?.onPlayerJoin(player)
    }

    fun execute(player: Player, command: String) {
        handlers.firstOrNull { it.canHandle(player) }?.execute(player, command)
    }

    fun suggest(player: Player, input: String): CompletableFuture<List<String>> {
        return handlers.firstOrNull { it.canHandle(player) }
            ?.suggest(player, input)
            ?: CompletableFuture.completedFuture(emptyList())
    }
}
