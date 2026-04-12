package ru.cherryngine.engine.minecraft.commandmanager

import io.micronaut.runtime.event.annotation.EventListener
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.commandmanager.CommandService
import ru.cherryngine.engine.minecraft.events.PacketEvent
import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundCommandSuggestionsPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound.ServerboundChatCommandPacket
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.serverbound.ServerboundCommandSuggestionPacket

@Singleton
class MinecraftCommandHandler(
    private val playerManager: PlayerManager,
    private val commandService: CommandService,
) {
    @EventListener
    fun onPacket(event: PacketEvent) {
        val (connection, packet) = event
        when (packet) {
            is ServerboundChatCommandPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid) ?: return
                commandService.execute(player, packet.command)
            }
            is ServerboundCommandSuggestionPacket -> {
                val player = playerManager.getPlayerNullable(connection.gameProfile.uuid) as? MinecraftPlayer ?: return
                val input = packet.text.removePrefix("/")
                commandService.suggest(player, input).whenComplete { suggestions, throwable ->
                    if (throwable != null) throw throwable
                    val lastSpace = input.lastIndexOf(' ')
                    val suggestionsPacket = ClientboundCommandSuggestionsPacket(
                        packet.transactionId,
                        lastSpace + 2,
                        input.length - lastSpace - 1,
                        suggestions.map { ClientboundCommandSuggestionsPacket.Suggestion(it, null) }
                    )
                    player.connection.sendPacket(suggestionsPacket)
                }
            }
        }
    }
}
