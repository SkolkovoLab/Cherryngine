package ru.cherryngine.engine.core.commandmanager

import io.leangen.geantyref.TypeToken
import kotlinx.coroutines.Dispatchers
import net.kyori.adventure.text.format.NamedTextColor
import org.incendo.cloud.CommandManager
import org.incendo.cloud.annotations.AnnotationParser
import org.incendo.cloud.exception.CommandExecutionException
import org.incendo.cloud.exception.handling.ExceptionHandler
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.internal.CommandRegistrationHandler
import org.incendo.cloud.kotlin.coroutines.annotations.installCoroutineSupport
import org.incendo.cloud.meta.CommandMeta
import org.incendo.cloud.meta.SimpleCommandMeta
import org.incendo.cloud.parser.ArgumentParser
import org.slf4j.LoggerFactory
import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.engine.core.utils.component

@InstanceSingleton
class CherryngineCommandManager(
    parsers: List<SArgumentParser<*>>,
) : CommandManager<CommandSender>(
    ExecutionCoordinator.coordinatorFor(ExecutionCoordinator.nonSchedulingExecutor()),
    CommandRegistrationHandler.nullCommandRegistrationHandler()
) {
    val annotationParser: AnnotationParser<CommandSender> = AnnotationParser(this, CommandSender::class.java)
        .installCoroutineSupport(context = Dispatchers.Unconfined)

    init {
        registerDefaultExceptionHandlers(
            { (context, caption, variables) ->
                val message = context.formatCaption(caption, variables).component(NamedTextColor.RED)
                context.sender().sendMessage(message)
            },
            { (message, throwable) ->
                logger.error(message, throwable)
            }
        )
        exceptionController().registerHandler(
            CommandExecutionException::class.java,
            ExceptionHandler.unwrappingHandler()
        )
        parsers.forEach { registerParser(it.type, it) }
    }

    override fun hasPermission(sender: CommandSender, permission: String) = true
    override fun createDefaultCommandMeta(): CommandMeta = SimpleCommandMeta.builder().build()

    fun registerCommands(vararg commands: Any) {
        annotationParser.parse(*commands)
    }

    @Suppress("UNCHECKED_CAST")
    fun registerParser(type: Class<*>, parser: ArgumentParser<CommandSender, *>) {
        parserRegistry().registerParserSupplier(
            TypeToken.get(type) as TypeToken<Any>
        ) { parser as ArgumentParser<CommandSender, Any> }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(CherryngineCommandManager::class.java)
    }
}
