package ru.cherryngine.engine.mcprotocollib

import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import org.cloudburstmc.math.vector.Vector3d
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.ClientboundSetEntityMotionPacket
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.entity.player.ClientboundPlayerPositionPacket
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerOutputProvider
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

@Singleton
class McProtocolLibPlayerOutputProvider(
    private val playerManager: PlayerManager,
) : PlayerOutputProvider {
    override fun teleport(uuid: UUID, position: Vec3D, yawPitch: YawPitch) {
        val player = playerManager.getPlayerNullable(uuid) as? McProtocolLibPlayer ?: return
        player.clientPosition = position
        player.clientYawPitch = yawPitch
        player.session.send(
            ClientboundPlayerPositionPacket(
                0,
                Vector3d.from(position.x, position.y, position.z),
                Vector3d.ZERO,
                yawPitch.yaw, yawPitch.pitch,
                emptyList()
            )
        )
    }

    override fun sendMessage(uuid: UUID, message: Component) {
        (playerManager.getPlayerNullable(uuid) as? McProtocolLibPlayer)
            ?.session?.send(ClientboundSystemChatPacket(message, false))
    }

    override fun setVelocity(uuid: UUID, velocity: Vec3D) {
        val player = playerManager.getPlayerNullable(uuid) as? McProtocolLibPlayer ?: return
        val vel = Vector3d.from(velocity.x / 20.0, velocity.y / 20.0, velocity.z / 20.0)
        player.session.send(ClientboundSetEntityMotionPacket(0, vel))
    }
}
