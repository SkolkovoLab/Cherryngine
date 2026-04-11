package ru.cherryngine.engine.minecraft.player

import ru.cherryngine.engine.core.PlayerInputProvider
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.UUID

class MinecraftPlayerInputProvider(
    private val playerManager: PlayerManager,
) : PlayerInputProvider {
    override fun getPosition(uuid: UUID): Vec3D? =
        (playerManager.getPlayerNullable(uuid) as? MinecraftPlayer)?.clientPosition

    override fun getYawPitch(uuid: UUID): YawPitch? =
        (playerManager.getPlayerNullable(uuid) as? MinecraftPlayer)?.clientYawPitch
}
