package ru.cherryngine.platform.minecraft.bedrock

import net.kyori.adventure.text.Component
import org.cloudburstmc.math.vector.Vector3f
import org.cloudburstmc.protocol.bedrock.packet.MovePlayerPacket
import org.cloudburstmc.protocol.bedrock.packet.SetEntityMotionPacket
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerOutputProvider
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.platform.minecraft.bedrock.utils.cloudburstVector3f
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
        packet.position = position.plus(0.0, 1.62, 0.0).cloudburstVector3f()
        packet.rotation = Vector3f.from(
            yawPitch.pitch,
            yawPitch.yaw,
            yawPitch.yaw
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
        packet.motion = (velocity / 20.0).cloudburstVector3f()
        player.session.sendPacket(packet)
    }
}
