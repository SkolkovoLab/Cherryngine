package ru.cherryngine.engine.core.shape

import ru.cherryngine.lib.math.Vec3D

sealed class ShapeGeometry {
    data class Box(val halfExtents: Vec3D) : ShapeGeometry()
    data class Capsule(val radius: Float, val halfHeight: Float) : ShapeGeometry()
    data class Sphere(val radius: Float) : ShapeGeometry()
    data class ConvexHull(val points: List<Vec3D>) : ShapeGeometry()
}
