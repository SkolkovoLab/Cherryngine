package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.command.PlayerSelector
import ac.grim.grimac.platform.api.manager.CommandAdapter
import ac.grim.grimac.platform.api.sender.Sender
import org.incendo.cloud.parser.ArgumentParser
import org.incendo.cloud.parser.ParserDescriptor
import org.incendo.cloud.suggestion.SuggestionProvider

class CommandAdapterImpl : CommandAdapter {
    override fun singlePlayerSelectorParser(): ParserDescriptor<Sender, PlayerSelector> {
        val parser = ArgumentParser<Sender, PlayerSelector> { commandContext, commandInput ->
            TODO("Not yet implemented")
        }
        return ParserDescriptor.parserDescriptor(parser, PlayerSelector::class.java)
    }

    override fun onlinePlayerSuggestions(): SuggestionProvider<Sender> {
        return SuggestionProvider.noSuggestions()
    }
}