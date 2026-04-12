package ru.cherryngine.engine.minecraft.commandmanager

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.commandmanager.CherryngineCommandManager
import ru.cherryngine.engine.core.commandmanager.CommandParserRegistrar
import ru.cherryngine.engine.core.commandmanager.SArgumentParser

@Singleton
class MinecraftParserRegistrar(
    private val parsers: List<SArgumentParser<*>>,
) : CommandParserRegistrar {
    override fun registerParsers(commandManager: CherryngineCommandManager) {
        parsers.forEach { commandManager.registerParser(it.type, it) }
    }
}
