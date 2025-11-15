package ru.cherryngine.engine.core.commandmanager

import io.micronaut.context.event.ApplicationEventListener
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
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
import org.incendo.cloud.suggestion.Suggestion
import org.slf4j.Logger
import ru.cherryngine.engine.core.Player
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.commandmanager.brigadier.CommandNodeUtils
import ru.cherryngine.engine.core.events.PacketEvent
import ru.cherryngine.engine.core.utils.component
import ru.cherryngine.lib.minecraft.protocol.packets.play.clientbound.ClientboundCommandSuggestionsPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundChatCommandPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundCommandSuggestionPacket
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundPlayerLoadedPacket
import ru.cherryngine.lib.minecraft.server.Connection

@Singleton
class CloudCommandManager(
    private val playerManager: PlayerManager,
    private val logger: Logger,
) : CommandManager<CommandSender>(
    ExecutionCoordinator.coordinatorFor(ExecutionCoordinator.nonSchedulingExecutor()),
    CommandRegistrationHandler.nullCommandRegistrationHandler()
), ApplicationEventListener<PacketEvent> {
    val annotationParser = AnnotationParser(this, CommandSender::class.java)
        .installCoroutineSupport(context = Dispatchers.Unconfined)

    @PostConstruct
    fun init() {
        // Обработка ошибок
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
    }

    override fun hasPermission(sender: CommandSender, permission: String): Boolean {
        return true
    }

    override fun createDefaultCommandMeta(): CommandMeta {
        return SimpleCommandMeta.builder().build()
    }

    fun onPlayerInit(connection: Connection) {
        val commandsPacket = CommandNodeUtils.commandsPacket(commandTree().rootNode())
        connection.sendPacket(commandsPacket)
    }

    private fun onCommandPacket(packet: ServerboundChatCommandPacket, player: Player) {
        commandExecutor().executeCommand(player, packet.command)
    }

    private fun onTabCompletePacket(packet: ServerboundCommandSuggestionPacket, player: Player) {
        val future = suggestionFactory().suggest(player, packet.text.removePrefix("/"))
        future.whenComplete { suggestions, throwable ->
            if (throwable != null) throw throwable
            val tabCompletePacket = ClientboundCommandSuggestionsPacket(
                packet.transactionId,
                suggestions.commandInput().cursor() + 1,
                suggestions.commandInput().length() + 1,
                suggestions.list().map { suggestion: Suggestion ->
                    ClientboundCommandSuggestionsPacket.Suggestion(suggestion.suggestion(), null)
                })
            player.connection.sendPacket(tabCompletePacket)
        }
    }

    override fun onApplicationEvent(event: PacketEvent) {
        val (connection, packet) = event
        when (packet) {
            is ServerboundChatCommandPacket -> onCommandPacket(packet, playerManager.getPlayer(connection))
            is ServerboundCommandSuggestionPacket -> onTabCompletePacket(packet, playerManager.getPlayer(connection))
            is ServerboundPlayerLoadedPacket -> onPlayerInit(connection)
        }
    }
}
