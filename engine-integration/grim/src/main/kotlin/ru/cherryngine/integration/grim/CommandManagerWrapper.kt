package ru.cherryngine.integration.grim

import ac.grim.grimac.platform.api.sender.SenderFactory
import io.leangen.geantyref.TypeToken
import org.incendo.cloud.Command
import org.incendo.cloud.CommandManager
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.SenderMapperHolder
import org.incendo.cloud.component.CommandComponent
import org.incendo.cloud.component.DefaultValue
import org.incendo.cloud.context.CommandContext
import org.incendo.cloud.context.CommandInput
import org.incendo.cloud.execution.CommandExecutionHandler
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.internal.CommandRegistrationHandler
import org.incendo.cloud.parser.ArgumentParseResult
import org.incendo.cloud.parser.ArgumentParser
import ru.cherryngine.engine.core.commandmanager.CommandSender
import ac.grim.grimac.platform.api.sender.Sender as GrimSender

class CommandManagerWrapper(
    private val originalManager: CommandManager<CommandSender>,
    private val senderFactory: SenderFactory<CommandSender>,
) : CommandManager<GrimSender>(
    ExecutionCoordinator.coordinatorFor(ExecutionCoordinator.nonSchedulingExecutor()),
    CommandRegistrationHandler.nullCommandRegistrationHandler()
), SenderMapperHolder<CommandSender, GrimSender> {
    private val senderMapper = SenderMapper.create(senderFactory::wrap, senderFactory::unwrap)
    override fun senderMapper() = senderMapper

    fun mapComponent(original: CommandComponent<GrimSender>): CommandComponent<CommandSender> {
        return CommandComponent.builder<CommandSender, Any>()
            .name(original.name())
            .parser(ArgumentParserWrapper(original.parser()))
            .description(original.description())
            .required(original.required())
            .defaultValue(original.defaultValue() as DefaultValue<CommandSender, Any>?)
            .valueType(original.valueType() as TypeToken<Any>)
            .build()
    }

    inner class ArgumentParserWrapper(
        val original: ArgumentParser<GrimSender, *>,
    ) : ArgumentParser<CommandSender, Any> {
        @Suppress("UNCHECKED_CAST")
        override fun parse(
            commandContext: CommandContext<CommandSender>,
            commandInput: CommandInput,
        ): ArgumentParseResult<Any> {
            return original.parse(
                mapCommandContext(commandContext),
                commandInput,
            ) as ArgumentParseResult<Any>
        }
    }

    fun mapCommandContext(original: CommandContext<CommandSender>): CommandContext<GrimSender> {
        return CommandContext(
            original.isSuggestions,
            senderMapper().map(original.sender()),
            this
        )
    }

    fun mapExecutionHandler(original: CommandExecutionHandler<GrimSender>): CommandExecutionHandler<CommandSender> {
        return CommandExecutionHandler { commandContext: CommandContext<CommandSender> ->
            original.execute(mapCommandContext(commandContext))
        }
    }

    fun mapCommand(original: Command<GrimSender>): Command<CommandSender> {
        return Command(
            original.components().map(::mapComponent),
            original.commandExecutionHandler().let(::mapExecutionHandler),
            null,
            original.commandPermission(),
            original.commandMeta(),
            original.commandDescription()
        )
    }

    override fun command(
        command: Command<out GrimSender>,
    ): CommandManager<GrimSender> {
        @Suppress("UNCHECKED_CAST")
        command as Command<GrimSender>
//        originalManager.command(mapCommand(command))
        return this
    }

    override fun hasPermission(sender: GrimSender, permission: String): Boolean {
        return originalManager.hasPermission(senderMapper().reverse(sender), permission)
    }
}
