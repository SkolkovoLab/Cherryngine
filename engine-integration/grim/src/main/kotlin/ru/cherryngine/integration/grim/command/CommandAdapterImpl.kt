package ru.cherryngine.integration.grim.command

import ac.grim.grimac.platform.api.command.PlayerSelector
import ac.grim.grimac.platform.api.manager.CommandAdapter
import jakarta.inject.Singleton
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.parser.ParserDescriptor
import org.incendo.cloud.suggestion.SuggestionProvider
import ru.cherryngine.engine.core.PlayerManager
import ac.grim.grimac.platform.api.sender.Sender as GrimSender

@Singleton
class CommandAdapterImpl(
    private val playerManager: PlayerManager,
    private val senderFactory: SenderFactoryImpl,
) : CommandAdapter {
    override fun singlePlayerSelectorParser(): ParserDescriptor<GrimSender, PlayerSelector> {
        val parser = ArgumentParser<GrimSender, PlayerSelector> { commandContext, commandInput ->
            val input = commandInput.peekString()
            val player = playerManager.getPlayerNullable(input)
            if (player == null) {
                ArgumentParseResult.failure(RuntimeException())
            } else {
                val grimSender = senderFactory.wrap(player)
                val playerSelector = PlayerSelectorImpl(grimSender, input)
                ArgumentParseResult.success(playerSelector)
            }
        }
        return ParserDescriptor.parserDescriptor(parser, PlayerSelector::class.java)
    }

    override fun onlinePlayerSuggestions(): SuggestionProvider<GrimSender> {
        val usernames = playerManager.onlinePlayers().map { it.username }
        return SuggestionProvider.suggestingStrings(usernames)
    }

    class PlayerSelectorImpl(
        val sender: GrimSender,
        val input: String,
    ) : PlayerSelector {
        override fun isSingle(): Boolean = true
        override fun getSinglePlayer(): GrimSender = sender
        override fun getPlayers(): Collection<GrimSender> = listOf(sender)
        override fun inputString(): String = input
    }
}