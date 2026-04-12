package ru.cherryngine.engine.core.services

import jakarta.inject.Singleton
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

interface SpawnSettings {
    val position: Vec3D get() = Vec3D.ZERO
    val yawPitch: YawPitch get() = YawPitch.ZERO
}

@Singleton
class DefaultSpawnSettings : SpawnSettings
