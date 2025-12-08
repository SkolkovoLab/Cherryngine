package ru.cherryngine.engine.physics

import com.github.stephengold.joltjni.Quat
import com.github.stephengold.joltjni.RVec3
import com.github.stephengold.joltjni.Vec3
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.rotation.QRot


fun Quat.qRot(): QRot = QRot(w.toDouble(), x.toDouble(), y.toDouble(), z.toDouble())
fun QRot.joltQuat(): Quat = Quat(x.toFloat(), y.toFloat(), z.toFloat(), w.toFloat())

fun Vec3.vec3D(): Vec3D = Vec3D(x.toDouble(), y.toDouble(), z.toDouble())
fun RVec3.vec3D(): Vec3D = Vec3D(xx(), yy(), zz())
fun Vec3D.joltVec3(): Vec3 = Vec3(x, y, z)
fun Vec3D.joltRVec3(): RVec3 = RVec3(x, y, z)