package ru.cherryngine.engine.core.commandmanager

fun interface CommandParserRegistrar {
    fun registerParsers(commandManager: CherryngineCommandManager)
}
