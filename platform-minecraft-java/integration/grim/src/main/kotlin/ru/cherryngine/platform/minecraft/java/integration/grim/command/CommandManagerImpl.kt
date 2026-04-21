package ru.cherryngine.platform.minecraft.java.integration.grim.command

import jakarta.inject.Singleton
import org.incendo.cloud.CommandManager
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.internal.CommandRegistrationHandler
import org.incendo.cloud.suggestion.Suggestion
import ru.cherryngine.engine.core.commandmanager.CommandSender
import java.util.concurrent.CompletableFuture
import ac.grim.grimac.platform.api.sender.Sender as GrimSender

/**
 * Глобальный cloud-менеджер Grim-команд (`CommandManager<GrimSender>`).
 *
 * Регистрация `/grim` в per-instance [ru.cherryngine.engine.core.commandmanager.CherryngineCommandManager]
 * вынесена в [GrimCommandBootstrap] — глобальный `@Singleton` не может инжектить per-instance bean
 * напрямую, поэтому bootstrap-бин с `@InstanceSingleton` создаётся для каждого minecraft-инстанса.
 */
@Singleton
class CommandManagerImpl(
    private val senderFactory: SenderFactoryImpl,
) : CommandManager<GrimSender>(
    ExecutionCoordinator.simpleCoordinator(),
    CommandRegistrationHandler.nullCommandRegistrationHandler()
) {
    fun init() {
        exceptionController().clearHandlers()
    }

    fun suggestions(
        context: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): CompletableFuture<Iterable<Suggestion>> {
        val sender = senderFactory.wrap(context.sender())
        val input = commandInput.input()
        val args = input.split(" ").let { it.subList(1, it.lastIndex) }
        return suggestionFactory().suggest(sender, input).thenApply { suggestions ->
            suggestions.list().map { suggestion ->
                Suggestion.suggestion((args + suggestion.suggestion()).joinToString(" "))
            }
        }
    }

    fun execute(
        context: CommandContext<CommandSender>
    ) {
        val sender = senderFactory.wrap(context.sender())
        val input = context.rawInput().input()
        val future = commandExecutor().executeCommand(sender, input)
        if (future.isCompletedExceptionally) throw future.exceptionNow()
    }

    override fun hasPermission(sender: GrimSender, permission: String): Boolean = true
}
