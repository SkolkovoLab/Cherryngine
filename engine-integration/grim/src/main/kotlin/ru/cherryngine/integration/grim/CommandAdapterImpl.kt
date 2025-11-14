package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.command.PlayerSelector
import ac.grim.grimac.platform.api.manager.CommandAdapter
import ac.grim.grimac.platform.api.sender.Sender
import org.incendo.cloud.parser.ParserDescriptor
import org.incendo.cloud.suggestion.SuggestionProvider

class CommandAdapterImpl : CommandAdapter {
    override fun singlePlayerSelectorParser(): ParserDescriptor<Sender, PlayerSelector> {
        TODO("Not yet implemented")
    }

    override fun onlinePlayerSuggestions(): SuggestionProvider<Sender> {
        TODO("Not yet implemented")
    }
}