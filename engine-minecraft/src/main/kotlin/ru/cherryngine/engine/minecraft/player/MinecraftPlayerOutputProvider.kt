package ru.cherryngine.engine.minecraft.player

import net.kyori.adventure.text.Component
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.engine.core.PlayerOutputProvider
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.UUID

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
