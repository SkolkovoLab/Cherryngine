package io.github.dockyardmc.cherry.math

import io.github.dockyardmc.cherry.math.rotation.QRot
import kotlinx.serialization.Serializable
import kotlin.math.abs
import kotlin.math.sqrt

@Serializable
data class Vec3D(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    constructor(xyz: Double) : this(xyz, xyz, xyz)

    companion object {
        val ZERO = Vec3D(0.0)
        val ONE = Vec3D(1.0)
        val PLUS_X = Vec3D(1.0, 0.0, 0.0)
        val MINUS_X = Vec3D(-1.0, 0.0, 0.0)
        val PLUS_Y = Vec3D(0.0, 1.0, 0.0)
        val MINUS_Y = Vec3D(0.0, -1.0, 0.0)
        val PLUS_Z = Vec3D(0.0, 0.0, 1.0)
        val MINUS_Z = Vec3D(0.0, 0.0, -1.0)

        const val EPSILON = 0.000001
    }

    //region with

    fun withX(operator: (Double) -> Double): Vec3D =
        Vec3D(operator(x), y, z)

    fun withY(operator: (Double) -> Double): Vec3D =
        Vec3D(x, operator(y), z)

    fun withZ(operator: (Double) -> Double): Vec3D =
        Vec3D(x, y, operator(z))

    //endregion
    //region plus

    fun plus(x: Double, y: Double, z: Double): Vec3D =
        Vec3D(this.x + x, this.y + y, this.z + z)

    operator fun plus(other: Vec3D): Vec3D =
        Vec3D(x + other.x, y + other.y, z + other.z)

    operator fun plus(value: Double): Vec3D =
        Vec3D(x + value, y + value, z + value)

    //endregion
    //region minus

    fun minus(x: Double, y: Double, z: Double): Vec3D =
        Vec3D(this.x - x, this.y - y, this.z - z)

    operator fun minus(other: Vec3D): Vec3D =
        Vec3D(x - other.x, y - other.y, z - other.z)

    operator fun minus(value: Double): Vec3D =
        Vec3D(x - value, y - value, z - value)

    //endregion
    //region times

    fun times(x: Double, y: Double, z: Double): Vec3D =
        Vec3D(this.x * x, this.y * y, this.z * z)

    operator fun times(other: Vec3D): Vec3D =
        Vec3D(x * other.x, y * other.y, z * other.z)

    operator fun times(value: Double): Vec3D =
        Vec3D(x * value, y * value, z * value)

    //endregion
    //region div

    fun div(x: Double, y: Double, z: Double): Vec3D =
        Vec3D(this.x / x, this.y / y, this.z / z)

    operator fun div(other: Vec3D): Vec3D =
        Vec3D(x / other.x, y / other.y, z / other.z)

    operator fun div(value: Double): Vec3D =
        Vec3D(x / value, y / value, z / value)

    //endregion

    operator fun unaryMinus(): Vec3D = Vec3D(-x, -y, -z)

    fun lengthSquared(): Double = x * x + y * y + z * z

    fun length(): Double = sqrt(lengthSquared())

    fun isNormalized(): Boolean = abs(lengthSquared() - 1) < EPSILON

    fun normalize(): Vec3D = if (!isNormalized()) div(length()) else this

    fun dot(vec: Vec3D): Double = x * vec.x + y * vec.y + z * vec.z

    fun cross(other: Vec3D): Vec3D =
        Vec3D(
            y * other.z - other.y * z,
            z * other.x - other.z * x,
            x * other.y - other.x * y
        )

    fun lerp(other: Vec3D, alpha: Double): Vec3D =
        Vec3D(
            x + (alpha * (other.x - x)),
            y + (alpha * (other.y - y)),
            z + (alpha * (other.z - z))
        )

    fun rotate(rot: QRot): Vec3D = rot.apply(this)
}