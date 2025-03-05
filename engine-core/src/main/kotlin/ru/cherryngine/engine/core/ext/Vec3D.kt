@file:Suppress("unused")

package ru.cherryngine.engine.core.ext

import net.minestom.server.coordinate.Pos
import net.minestom.server.coordinate.Vec
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View

fun Vec3D.minestomVec() = Vec(x, y, z)
fun Vec3D.minestomPos() = Pos(x, y, z)
fun Vec3D.minestomPos(yaw: Float, pitch: Float) = Pos(x, y, z, yaw, pitch)
fun Vec3D.minestomPos(view: View) = Pos(x, y, z, view.yaw, view.pitch)
