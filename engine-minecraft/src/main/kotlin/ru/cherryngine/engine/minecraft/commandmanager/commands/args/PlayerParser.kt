package ru.cherryngine.engine.minecraft.commandmanager.commands.args

import jakarta.inject.Singleton
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.suggestion.BlockingSuggestionProvider
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.minecraft.commandmanager.SArgumentParser
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.lib.minecraft.network.protocol.types.ArgumentParserType

@Singleton
class PlayerParser(
    private val playerManager: PlayerManager,
) : SArgumentParser<MinecraftPlayer>, BlockingSuggestionProvider.Strings<CommandSender> {
    override val type: Class<MinecraftPlayer> = MinecraftPlayer::class.java
    override val argumentParserType: ArgumentParserType = ArgumentParserType.GameProfile

    override fun parse(
        commandContext: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): ArgumentParseResult<MinecraftPlayer> {
        val input = commandInput.readString()
        val player = playerManager.getPlayerNullable(input) as? MinecraftPlayer
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
