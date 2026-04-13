package ru.cherryngine.engine.physics.terrain

import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D

data class ActiveBodyInfo(
    val aabb: Cuboid,
    val velocity: Vec3D,
    val physContextIDs: Set<String>,
)