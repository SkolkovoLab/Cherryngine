package ru.cherryngine.platform.minecraft.bedrock.utils

import org.cloudburstmc.math.vector.Vector3d
import org.cloudburstmc.math.vector.Vector3f
import ru.cherryngine.lib.math.Vec3D


fun Vec3D.cloudburstVector3d(): Vector3d = Vector3d.from(x, y, z)
fun Vec3D.cloudburstVector3f(): Vector3f = Vector3f.from(x, y, z)

fun Vector3d.vec3D() = Vec3D(x, y, z)
fun Vector3f.vec3D() = Vec3D(x.toDouble(), y.toDouble(), z.toDouble())
