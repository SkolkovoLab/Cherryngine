package ru.cherryngine.engine.mcprotocollib

import org.geysermc.mcprotocollib.auth.GameProfile
import org.geysermc.mcprotocollib.network.Session
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter
import org.geysermc.mcprotocollib.network.packet.Packet
import org.geysermc.mcprotocollib.protocol.MinecraftConstants
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundCommandSuggestionPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerPosRotPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundMovePlayerRotPacket
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

class McProtocolLibSessionListener(
    private val playerManager: PlayerManager,
) : SessionAdapter() {
    override fun packetReceived(session: Session, packet: Packet) {
        val profile = session.getFlag(MinecraftConstants.PROFILE_KEY) as? GameProfile ?: return
        val player = playerManager.getPlayerNullable(profile.id) as? McProtocolLibPlayer ?: return

        when (packet) {
            is ServerboundMovePlayerPosPacket -> {
                player.clientPosition = Vec3D(packet.x, packet.y, packet.z)
            }
            is ServerboundMovePlayerPosRotPacket -> {
                player.clientPosition = Vec3D(packet.x, packet.y, packet.z)
                player.clientYawPitch = YawPitch(packet.yaw, packet.pitch)
            }
            is ServerboundMovePlayerRotPacket -> {
                player.clientYawPitch = YawPitch(packet.yaw, packet.pitch)
            }
            is ServerboundChatCommandPacket -> {
                player.pendingCommands.offer(packet.command)
            }
            is ServerboundCommandSuggestionPacket -> {
                player.pendingSuggestions.offer(Pair(packet.transactionId, packet.text.removePrefix("/")))
            }
        }
    }
}
