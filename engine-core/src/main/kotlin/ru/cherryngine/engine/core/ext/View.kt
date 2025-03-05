@file:Suppress("unused")

package ru.cherryngine.engine.core.ext

import net.minestom.server.coordinate.Pos
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.View

fun View.minestomPos(pos: Vec3D) = Pos(pos.x, pos.y, pos.z, yaw, pitch)
