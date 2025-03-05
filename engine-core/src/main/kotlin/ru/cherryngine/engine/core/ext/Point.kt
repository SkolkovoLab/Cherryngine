@file:Suppress("unused")

package ru.cherryngine.engine.core.ext

import net.minestom.server.coordinate.Point
import ru.cherryngine.lib.math.Vec3D

fun Point.asVec3D() = Vec3D(x(), y(), z())
