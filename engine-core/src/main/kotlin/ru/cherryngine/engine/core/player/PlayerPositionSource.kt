package ru.cherryngine.engine.core.player

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

data class PositionSnapshot(val position: Vec3D, val yawPitch: YawPitch)

interface PlayerPositionSource {
    fun canHandle(player: Player): Boolean
    fun getDesired(player: Player): PositionSnapshot?
    fun acceptClientMovement(player: Player, position: Vec3D, yawPitch: YawPitch)
}
