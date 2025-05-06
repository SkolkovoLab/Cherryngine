package ru.cherryngine.lib.math.rotation

import ru.cherryngine.lib.math.Vec3D

data class AxisAngleSequence(
    val axisSequence: AxisSequence,
    val angle1: Double,
    val angle2: Double,
    val angle3: Double,
) {
    companion object {
        fun xyz(vec: Vec3D) = AxisAngleSequence(AxisSequence.XYZ, vec.x, vec.y, vec.z)
        fun xzy(vec: Vec3D) = AxisAngleSequence(AxisSequence.XZY, vec.x, vec.z, vec.y)
        fun yxz(vec: Vec3D) = AxisAngleSequence(AxisSequence.YXZ, vec.y, vec.x, vec.z)
        fun yzx(vec: Vec3D) = AxisAngleSequence(AxisSequence.YZX, vec.y, vec.z, vec.x)
        fun zxy(vec: Vec3D) = AxisAngleSequence(AxisSequence.ZXY, vec.z, vec.x, vec.y)
        fun zyx(vec: Vec3D) = AxisAngleSequence(AxisSequence.ZYX, vec.z, vec.y, vec.x)
    }

    fun toVec3D() = when (axisSequence) {
        AxisSequence.XYZ -> Vec3D(angle1, angle2, angle3)
        AxisSequence.XZY -> Vec3D(angle1, angle3, angle2)
        AxisSequence.YXZ -> Vec3D(angle2, angle1, angle3)
        AxisSequence.YZX -> Vec3D(angle3, angle1, angle2)
        AxisSequence.ZXY -> Vec3D(angle2, angle3, angle1)
        AxisSequence.ZYX -> Vec3D(angle3, angle2, angle1)
        else -> throw IllegalArgumentException()
    }

    fun toQRot() = QRot.fromAxisAngleSequence(this)
}