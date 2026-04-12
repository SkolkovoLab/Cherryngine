package ru.cherryngine.engine.minecraft.player

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.player.PlayerInputProvider
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

@Singleton
class MinecraftPlayerInputProvider(
    private val playerManager: PlayerManager,
) : PlayerInputProvider {
    override fun getPosition(uuid: UUID): Vec3D? =
        (playerManager.getPlayerNullable(uuid) as? MinecraftPlayer)?.clientPosition

    override fun getYawPitch(uuid: UUID): YawPitch? =
        (playerManager.getPlayerNullable(uuid) as? MinecraftPlayer)?.clientYawPitch
}
