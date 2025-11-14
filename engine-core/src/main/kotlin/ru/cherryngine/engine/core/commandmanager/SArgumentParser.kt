package ru.cherryngine.engine.core.commandmanager

import org.incendo.cloud.parser.ArgumentParser
import ru.cherryngine.lib.minecraft.protocol.types.ArgumentParserType

interface SArgumentParser<T> : ArgumentParser<CommandSender, T> {
    val type: Class<T>
    val argumentParserType: ArgumentParserType
    val serverSuggestions: Boolean get() = true
}