package ru.cherryngine.engine.core

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import java.util.UUID

interface PlayerInputProvider {
    fun getPosition(uuid: UUID): Vec3D?
    fun getYawPitch(uuid: UUID): YawPitch?
}
