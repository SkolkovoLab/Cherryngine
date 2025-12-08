package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I

enum class Direction(
    val vec: Vec3I,
) {
    DOWN(Vec3I(0, -1, 0)),
    UP(Vec3I(0, 1, 0)),
    NORTH(Vec3I(0, 0, -1)),
    SOUTH(Vec3I(0, 0, 1)),
    WEST(Vec3I(-1, 0, 0)),
    EAST(Vec3I(1, 0, 0));

    val vec3D = Vec3D(vec.x.toDouble(), vec.y.toDouble(), vec.z.toDouble())
}