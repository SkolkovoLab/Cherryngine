package ru.cherryngine.engine.bedrock

import net.kyori.adventure.text.Component
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerOutputProvider
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

class BedrockPlayerOutputProvider(
    private val playerManager: PlayerManager,
) : PlayerOutputProvider {
    override fun teleport(uuid: UUID, position: Vec3D, yawPitch: YawPitch) {
        val player = playerManager.getPlayerNullable(uuid) as? BedrockPlayer ?: return
        player.clientPosition = position
        player.clientYawPitch = yawPitch
        val packet = MovePlayerPacket()
        packet.runtimeEntityId = player.runtimeEntityId
        packet.position = Vector3f.from(
            position.x.toFloat(),
            (position.y + 1.62).toFloat(),
            position.z.toFloat()
        )
        packet.rotation = Vector3f.from(
            yawPitch.pitch.toFloat(),
            yawPitch.yaw.toFloat(),
            yawPitch.yaw.toFloat()
        )
        packet.mode = MovePlayerPacket.Mode.TELEPORT
        packet.teleportationCause = MovePlayerPacket.TeleportationCause.COMMAND
        player.session.sendPacket(packet)
    }

    override fun sendMessage(uuid: UUID, message: Component) {
        playerManager.getPlayerNullable(uuid)?.sendMessage(message)
    }

    override fun setVelocity(uuid: UUID, velocity: Vec3D) {
        val player = playerManager.getPlayerNullable(uuid) as? BedrockPlayer ?: return
        val packet = SetEntityMotionPacket()
        packet.runtimeEntityId = player.runtimeEntityId
        packet.motion = Vector3f.from(
            velocity.x.toFloat(),
            velocity.y.toFloat(),
            velocity.z.toFloat()
        )
        player.session.sendPacket(packet)
    }
}
