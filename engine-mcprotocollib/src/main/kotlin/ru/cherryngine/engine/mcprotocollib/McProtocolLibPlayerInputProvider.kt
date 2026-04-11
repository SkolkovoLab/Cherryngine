package ru.cherryngine.engine.mcprotocollib

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.PlayerInputProvider
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.UUID

@Singleton
class McProtocolLibPlayerInputProvider(
    private val playerManager: PlayerManager,
) : PlayerInputProvider {
    override fun getPosition(uuid: UUID): Vec3D? =
        (playerManager.getPlayerNullable(uuid) as? McProtocolLibPlayer)?.clientPosition

    override fun getYawPitch(uuid: UUID): YawPitch? =
        (playerManager.getPlayerNullable(uuid) as? McProtocolLibPlayer)?.clientYawPitch
}
