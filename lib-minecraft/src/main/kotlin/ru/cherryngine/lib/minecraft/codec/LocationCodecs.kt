package ru.cherryngine.lib.minecraft.codec

import ru.cherryngine.lib.math.Vec3I

object LocationCodecs {
    val VEC3I_ARRAY = Codec.INT_ARRAY.transform(
        { from -> Vec3I(from[0], from[1], from[2]) },
        { to -> intArrayOf(to.x, to.y, to.z) }
    )
}