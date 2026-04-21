package ru.cherryngine.platform.minecraft.java.utils

import net.minestom.server.collision.BoundingBox
import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch
import ru.cherryngine.lib.math.rotation.QRot


fun Vec3D.minestomVec(): Vec = Vec(x, y, z)
fun Vec3D.minestomPos(yawPitch: YawPitch = YawPitch.ZERO): Pos = Pos(x, y, z, yawPitch.yaw, yawPitch.pitch)

fun Point.vec3D() = Vec3D(x(), y(), z())
fun Pos.yawPitch() = YawPitch(yaw, pitch)

fun BoundingBox.min() = relativeStart.vec3D()
fun BoundingBox.max() = relativeEnd.vec3D()
fun BoundingBox.cuboid() = Cuboid(min(), max())

fun Cuboid.minestomBoundingBox() = BoundingBox(min.minestomVec(), max.minestomVec())

fun QRot.minestomQuaternion(): FloatArray = floatArrayOf(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())