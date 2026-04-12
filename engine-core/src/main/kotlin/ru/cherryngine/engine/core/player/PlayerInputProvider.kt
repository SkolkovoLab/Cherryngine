package ru.cherryngine.engine.core.player

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.*

interface PlayerInputProvider {
    fun getPosition(uuid: UUID): Vec3D?
    fun getYawPitch(uuid: UUID): YawPitch?
}
