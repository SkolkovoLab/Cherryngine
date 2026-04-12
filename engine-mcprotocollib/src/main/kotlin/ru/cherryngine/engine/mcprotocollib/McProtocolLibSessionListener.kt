package ru.cherryngine.engine.mcprotocollib

import org.geysermc.mcprotocollib.auth.GameProfile
import org.geysermc.mcprotocollib.network.Session
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter
import org.geysermc.mcprotocollib.network.packet.Packet
import org.geysermc.mcprotocollib.protocol.MinecraftConstants
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundCommandSuggestionsPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundCommandSuggestionPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerRotPacket
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.commandmanager.CommandService
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

class McProtocolLibSessionListener(
    private val playerManager: PlayerManager,
    private val commandService: CommandService,
) : SessionAdapter() {
    override fun packetReceived(session: Session, packet: Packet) {
        val profile = session.getFlag(MinecraftConstants.PROFILE_KEY) as? GameProfile ?: return
        val player = playerManager.getPlayerNullable(profile.id) ?: return

        when (packet) {
            is ServerboundMovePlayerPosPacket -> {
                (player as McProtocolLibPlayer).clientPosition = Vec3D(packet.x, packet.y, packet.z)
            }
            is ServerboundMovePlayerPosRotPacket -> {
                val mcplPlayer = player as McProtocolLibPlayer
                mcplPlayer.clientPosition = Vec3D(packet.x, packet.y, packet.z)
                mcplPlayer.clientYawPitch = YawPitch(packet.yaw, packet.pitch)
            }
            is ServerboundMovePlayerRotPacket -> {
                (player as McProtocolLibPlayer).clientYawPitch = YawPitch(packet.yaw, packet.pitch)
            }
            is ServerboundChatCommandPacket -> {
                commandService.execute(player, packet.command)
            }
            is ServerboundCommandSuggestionPacket -> {
                val input = packet.text.removePrefix("/")
                commandService.suggest(player, input).whenComplete { suggestions, throwable ->
                    if (throwable != null) throw throwable
                    val lastSpace = input.lastIndexOf(' ')
                    session.send(ClientboundCommandSuggestionsPacket(
                        packet.transactionId,
                        lastSpace + 2,
                        input.length - lastSpace - 1,
                        suggestions.toTypedArray(),
                        arrayOfNulls(suggestions.size)
                    ))
                }
            }
        }
    }
}
