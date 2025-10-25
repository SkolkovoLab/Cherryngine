package ru.cherryngine.lib.math

data class Vec3I(
    val x: Int,
    val y: Int,
    val z: Int,
) {
    constructor(xyz: Int) : this(xyz, xyz, xyz)

    companion object {
        val ZERO = Vec3I(0)
        val ONE = Vec3I(1)
        val PLUS_X = Vec3I(1, 0, 0)
        val MINUS_X = Vec3I(-1, 0, 0)
        val PLUS_Y = Vec3I(0, 1, 0)
        val MINUS_Y = Vec3I(0, -1, 0)
        val PLUS_Z = Vec3I(0, 0, 1)
        val MINUS_Z = Vec3I(0, 0, -1)

        const val EPSILON = 0.000001
    }

    //region with

    fun withX(operator: (Int) -> Int): Vec3I =
        Vec3I(operator(x), y, z)

    fun withY(operator: (Int) -> Int): Vec3I =
        Vec3I(x, operator(y), z)

    fun withZ(operator: (Int) -> Int): Vec3I =
        Vec3I(x, y, operator(z))

    //endregion
    //region plus

    fun plus(x: Int, y: Int, z: Int): Vec3I =
        Vec3I(this.x + x, this.y + y, this.z + z)

    operator fun plus(other: Vec3I): Vec3I =
        Vec3I(x + other.x, y + other.y, z + other.z)

    operator fun plus(value: Int): Vec3I =
        Vec3I(x + value, y + value, z + value)

    //endregion
    //region minus

    fun minus(x: Int, y: Int, z: Int): Vec3I =
        Vec3I(this.x - x, this.y - y, this.z - z)

    operator fun minus(other: Vec3I): Vec3I =
        Vec3I(x - other.x, y - other.y, z - other.z)

    operator fun minus(value: Int): Vec3I =
        Vec3I(x - value, y - value, z - value)

    //endregion
    //region times

    fun times(x: Int, y: Int, z: Int): Vec3I =
        Vec3I(this.x * x, this.y * y, this.z * z)

    operator fun times(other: Vec3I): Vec3I =
        Vec3I(x * other.x, y * other.y, z * other.z)

    operator fun times(value: Int): Vec3I =
        Vec3I(x * value, y * value, z * value)

    //endregion
    //region div

    fun div(x: Int, y: Int, z: Int): Vec3I =
        Vec3I(this.x / x, this.y / y, this.z / z)

    operator fun div(other: Vec3I): Vec3I =
        Vec3I(x / other.x, y / other.y, z / other.z)

    operator fun div(value: Int): Vec3I =
        Vec3I(x / value, y / value, z / value)

    //endregion

    operator fun unaryMinus(): Vec3I = Vec3I(-x, -y, -z)
}