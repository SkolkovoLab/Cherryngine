package ru.cherryngine.engine.core.commandmanager.args

import jakarta.inject.Singleton
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.suggestion.BlockingSuggestionProvider
import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.commandmanager.SArgumentParser

@Singleton
class PlayerParser(
    private val playerManager: PlayerManager,
) : SArgumentParser<Player>, BlockingSuggestionProvider.Strings<CommandSender> {
    override val type: Class<Player> = Player::class.java

    override fun parse(
        commandContext: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): ArgumentParseResult<Player> {
        val input = commandInput.readString()
        val player = playerManager.getPlayerNullable(input)
            ?: return ArgumentParseResult.failure(IllegalArgumentException("Player '$input' not found"))
        return ArgumentParseResult.success(player)
    }

    override fun stringSuggestions(
        commandContext: CommandContext<CommandSender?>,
        input: CommandInput,
    ): Iterable<String> {
        return playerManager.onlinePlayers().map { it.username }.toList()
    }
}