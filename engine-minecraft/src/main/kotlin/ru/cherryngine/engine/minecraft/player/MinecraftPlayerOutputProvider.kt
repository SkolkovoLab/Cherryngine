package ru.cherryngine.engine.minecraft.player

import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerOutputProvider
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.minecraft.network.protocol.packets.play.clientbound.ClientboundSetEntityMotionPacket
import java.util.*

@Singleton
class MinecraftPlayerOutputProvider(
    private val playerManager: PlayerManager,
) : PlayerOutputProvider {
    override fun teleport(uuid: UUID, position: Vec3D, yawPitch: YawPitch) {
        (playerManager.getPlayerNullable(uuid) as? MinecraftPlayer)?.teleport(position, yawPitch)
    }

    override fun sendMessage(uuid: UUID, message: Component) {
        playerManager.getPlayerNullable(uuid)?.sendMessage(message)
    }

    override fun setVelocity(uuid: UUID, velocity: Vec3D) {
        val player = playerManager.getPlayerNullable(uuid) as? MinecraftPlayer ?: return
        player.connection.sendPacket(ClientboundSetEntityMotionPacket(0, velocity.div(20.0)))
    }
}
