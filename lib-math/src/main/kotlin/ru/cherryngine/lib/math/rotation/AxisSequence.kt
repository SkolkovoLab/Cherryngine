package ru.cherryngine.lib.math.rotation

import ru.cherryngine.lib.math.Vec3D

enum class AxisSequence(
    val type: Type,
    val axis1: Vec3D,
    val axis2: Vec3D,
    val axis3: Vec3D,
) {
    XYZ(Type.TAIT_BRYAN, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_Z),
    XZY(Type.TAIT_BRYAN, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_Y),
    YXZ(Type.TAIT_BRYAN, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Z),
    YZX(Type.TAIT_BRYAN, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_X),
    ZXY(Type.TAIT_BRYAN, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Y),
    ZYX(Type.TAIT_BRYAN, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_X),
    XYX(Type.EULER, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_X),
    XZX(Type.EULER, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_X),
    YXY(Type.EULER, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Y),
    YZY(Type.EULER, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_Y),
    ZXZ(Type.EULER, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_X, Vec3D.Companion.PLUS_Z),
    ZYZ(Type.EULER, Vec3D.Companion.PLUS_Z, Vec3D.Companion.PLUS_Y, Vec3D.Companion.PLUS_Z);

    enum class Type {
        TAIT_BRYAN,
        EULER,
    }
}