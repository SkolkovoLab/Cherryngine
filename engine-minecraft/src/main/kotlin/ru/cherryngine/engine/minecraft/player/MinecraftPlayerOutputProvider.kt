package ru.cherryngine.engine.minecraft.player

import jakarta.inject.Singleton
import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.engine.core.player.PlayerOutputProvider
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
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
}
