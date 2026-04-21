package ru.cherryngine.platform.minecraft.java.integration.grim.command

import org.incendo.cloud.parser.standard.StringParser
import org.incendo.cloud.suggestion.SuggestionProvider
import ru.cherryngine.engine.core.commandmanager.CherryngineCommandManager
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ru.cherryngine.engine.core.instance.InstanceSingleton

/**
 * Per-instance bootstrap: для каждого minecraft-инстанса регистрирует greedy-args
 * команду `/grim` (+ aliases `grimac`, `gl`) в его [CherryngineCommandManager].
 *
 * Запускается через `Instance.initEager()`, где [InstanceSingletonScope] уже активен
 * — соответственно, per-instance `cherryngineManager` резолвится корректно. Глобальный
 * [CommandManagerImpl] переиспользуется из root application context'а.
 */
@InstanceSingleton(eagerInit = true, platform = "minecraft")
class GrimCommandBootstrap(
    private val cherryngineManager: CherryngineCommandManager,
    private val commandManagerImpl: CommandManagerImpl,
) {
    init {
        val stringParser = StringParser.greedyStringParser<CommandSender>()
        val suggestionProvider = SuggestionProvider(commandManagerImpl::suggestions)
        val command = cherryngineManager.commandBuilder("grim", "grimac", "gl")
            .optional("args", stringParser, suggestionProvider)
            .handler { commandManagerImpl.execute(it) }
            .build()
        cherryngineManager.command(command)
    }
}
