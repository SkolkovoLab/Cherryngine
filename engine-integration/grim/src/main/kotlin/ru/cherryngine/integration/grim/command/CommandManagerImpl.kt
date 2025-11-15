package ru.cherryngine.integration.grim.command

import jakarta.inject.Singleton
import org.incendo.cloud.CommandManager
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.internal.CommandRegistrationHandler
import org.incendo.cloud.parser.standard.StringParser
import org.incendo.cloud.suggestion.Suggestion
import org.incendo.cloud.suggestion.SuggestionProvider
import ru.cherryngine.engine.core.commandmanager.CommandSender
import java.util.concurrent.CompletableFuture
import ac.grim.grimac.platform.api.sender.Sender as GrimSender

@Singleton
class CommandManagerImpl(
    private val originalManager: CommandManager<CommandSender>,
    private val senderFactory: SenderFactoryImpl,
) : CommandManager<GrimSender>(
    ExecutionCoordinator.coordinatorFor(ExecutionCoordinator.nonSchedulingExecutor()),
    CommandRegistrationHandler.nullCommandRegistrationHandler()
) {
    init {
        val cmdName = "grim"
        val alias = arrayOf("grimac")

        val stringParser = StringParser.stringParser<CommandSender>(StringParser.StringMode.GREEDY)
        val suggestionProvider = SuggestionProvider(::suggestions)

        val command = originalManager.commandBuilder(cmdName, *alias)
            .optional("args", stringParser, suggestionProvider)
            .handler { execute(cmdName, it) }
            .build()
        originalManager.command(command)
    }

    fun suggestions(
        context: CommandContext<CommandSender>,
        commandInput: CommandInput,
    ): CompletableFuture<Iterable<Suggestion>> {
        val sender = senderFactory.wrap(context.sender())
        return suggestionFactory().suggest(sender, commandInput.readInput()).thenApply { it.list() }
    }

    fun execute(
        cmdName: String,
        context: CommandContext<CommandSender>,
    ) {
        val sender = senderFactory.wrap(context.sender())
        val input = context.getOrDefault("args", "")
        commandExecutor().executeCommand(sender, "$cmdName $input")
    }

    override fun hasPermission(sender: GrimSender, permission: String): Boolean {
        return originalManager.hasPermission(senderFactory.unwrap(sender), permission)
    }
}
