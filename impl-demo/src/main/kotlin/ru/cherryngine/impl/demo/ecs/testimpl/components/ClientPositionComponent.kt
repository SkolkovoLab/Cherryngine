package ru.cherryngine.impl.demo.ecs.testimpl.components

import ru.cherryngine.impl.demo.ecs.GameComponent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

data class ClientPositionComponent(
    val clientPosition: Vec3D,
    val clientYawPitch: YawPitch,
) : GameComponent