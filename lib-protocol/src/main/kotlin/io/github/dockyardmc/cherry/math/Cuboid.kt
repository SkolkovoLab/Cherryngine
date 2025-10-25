package io.github.dockyardmc.cherry.math

import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

data class Cuboid(
    val min: Vec3D,
    val max: Vec3D,
) {
    companion object {
        fun fromTwoPoints(v1: Vec3D, v2: Vec3D): Cuboid {
            val min = Vec3D(min(v1.x, v2.x), min(v1.y, v2.y), min(v1.z, v2.z))
            val max = Vec3D(max(v1.x, v2.x), max(v1.y, v2.y), max(v1.z, v2.z))
            return Cuboid(min, max)
        }

        fun fromCenterAndSize(center: Vec3D, size: Vec3D): Cuboid {
            val halfSize = size * 0.5
            return Cuboid(center - halfSize, center + halfSize)
        }

        fun fromSize(size: Vec3D): Cuboid {
            return fromCenterAndSize(Vec3D.ZERO, size)
        }
    }

    val center: Vec3D
        get() = Vec3D(centerX, centerY, centerZ)
    val size: Vec3D
        get() = Vec3D(sizeX, sizeY, sizeZ)

    val sizeX: Double
        get() = max.x - min.x
    val sizeY: Double
        get() = max.y - min.y
    val sizeZ: Double
        get() = max.z - min.z

    val centerX: Double
        get() = min.x + sizeX * 0.5
    val centerY: Double
        get() = min.y + sizeY * 0.5
    val centerZ: Double
        get() = min.z + sizeZ * 0.5

    /**
     * Возвращает кубоид у которого точки по центру блока, удобно когда игроку надо отобразить, можно вызвать этот метод,
     * а затем вызвать getCorners()
     */
    fun toBlockCenterCuboid(): Cuboid {
        return Cuboid(
            Vec3D(
                floor(min.x) + 0.5,
                floor(min.y) + 0.5,
                floor(min.z) + 0.5
            ),
            Vec3D(
                floor(max.x) + 0.5,
                floor(max.y) + 0.5,
                floor(max.z) + 0.5
            )
        )
    }

    fun getCorners() = listOf(
        Vec3D(min.x, min.y, min.z),
        Vec3D(min.x, min.y, max.z),
        Vec3D(min.x, max.y, min.z),
        Vec3D(min.x, max.y, max.z),
        Vec3D(max.x, min.y, min.z),
        Vec3D(max.x, min.y, max.z),
        Vec3D(max.x, max.y, min.z),
        Vec3D(max.x, max.y, max.z)
    )

    fun isInside(loc: Vec3D): Boolean {
        val moreThatMin = loc.x >= min.x && loc.y >= min.y && loc.z >= min.z
        val lessThatMax = loc.x <= max.x && loc.y <= max.y && loc.z <= max.z
        return moreThatMin && lessThatMax
    }

    @Suppress("DuplicatedCode")
    fun expand(
        negativeX: Double,
        negativeY: Double,
        negativeZ: Double,
        positiveX: Double,
        positiveY: Double,
        positiveZ: Double,
    ): Cuboid {
        if (negativeX == 0.0 && negativeY == 0.0 && negativeZ == 0.0 && positiveX == 0.0 && positiveY == 0.0 && positiveZ == 0.0) {
            return this
        }
        var newMinX: Double = min.x - negativeX
        var newMinY: Double = min.y - negativeY
        var newMinZ: Double = min.z - negativeZ
        var newMaxX: Double = max.x + positiveX
        var newMaxY: Double = max.y + positiveY
        var newMaxZ: Double = max.z + positiveZ

        // limit shrinking:
        if (newMinX > newMaxX) {
            val centerX = centerX
            if (newMaxX >= centerX) {
                newMinX = newMaxX
            } else if (newMinX <= centerX) {
                newMaxX = newMinX
            } else {
                newMinX = centerX
                newMaxX = centerX
            }
        }
        if (newMinY > newMaxY) {
            val centerY = centerY
            if (newMaxY >= centerY) {
                newMinY = newMaxY
            } else if (newMinY <= centerY) {
                newMaxY = newMinY
            } else {
                newMinY = centerY
                newMaxY = centerY
            }
        }
        if (newMinZ > newMaxZ) {
            val centerZ = centerZ
            if (newMaxZ >= centerZ) {
                newMinZ = newMaxZ
            } else if (newMinZ <= centerZ) {
                newMaxZ = newMinZ
            } else {
                newMinZ = centerZ
                newMaxZ = centerZ
            }
        }
        return Cuboid(Vec3D(newMinX, newMinY, newMinZ), Vec3D(newMaxX, newMaxY, newMaxZ))
    }

    fun expand(expansion: Double): Cuboid {
        return this.expand(expansion, expansion, expansion, expansion, expansion, expansion)
    }

    fun expand(x: Double, y: Double, z: Double): Cuboid {
        return this.expand(x, y, z, x, y, z)
    }
}
