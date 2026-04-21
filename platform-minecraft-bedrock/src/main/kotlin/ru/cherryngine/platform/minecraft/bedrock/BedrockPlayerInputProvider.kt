package ru.cherryngine.platform.minecraft.bedrock

import ru.cherryngine.engine.core.player.PlayerInputProvider
import ru.cherryngine.engine.core.player.PlayerManager
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

class BedrockPlayerInputProvider(
    private val playerManager: PlayerManager,
) : PlayerInputProvider {
    override fun getPosition(uuid: UUID): Vec3D? =
        (playerManager.getPlayerNullable(uuid) as? BedrockPlayer)?.clientPosition

    override fun getYawPitch(uuid: UUID): YawPitch? =
        (playerManager.getPlayerNullable(uuid) as? BedrockPlayer)?.clientYawPitch
}
