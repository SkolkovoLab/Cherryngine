package ru.cherryngine.engine.core.commandmanager

import ru.cherryngine.engine.core.player.Player
import java.util.concurrent.CompletableFuture

interface CommandServiceHandler {
    fun canHandle(player: Player): Boolean
    fun onPlayerJoin(player: Player)
    fun onPlayerLeave(player: Player)
    fun execute(player: Player, command: String)
    fun suggest(player: Player, input: String): CompletableFuture<List<String>>
}
