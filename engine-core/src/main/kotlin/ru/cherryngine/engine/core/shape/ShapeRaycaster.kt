package ru.cherryngine.engine.core.shape

import ru.cherryngine.engine.core.instance.InstanceSingleton
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D

@InstanceSingleton
class ShapeRaycaster(private val shapeWorld: ShapeWorld) {

    fun raycast(
        from: Vec3D,
        direction: Vec3D,
        maxDistance: Double,
        request: RayCastRequest,
        filter: ShapeFilter = ShapeFilter.ALL,
    ) {
        val dir = direction.normalize()
        val shapes = shapeWorld.query(filter)
            .mapNotNull { resolved ->
                val dist = intersect(from, dir, resolved) ?: return@mapNotNull null
                dist to resolved
            }
            .sortedBy { it.first.inletDistance }

        for ((rayInfo, resolved) in shapes) {
            if (request.onShapeEnter(rayInfo, resolved)) {
                request.onFinish(RayCastRequest.FinishReason.STOPPED_ON_ENTER)
                return
            }
            if (request.onShapeExit(rayInfo, resolved)) {
                request.onFinish(RayCastRequest.FinishReason.STOPPED_ON_EXIT)
                return
            }
        }
        request.onFinish(RayCastRequest.FinishReason.MAX_DISTANCE)
    }

    private fun intersect(from: Vec3D, dir: Vec3D, resolved: ResolvedShape): RayInfo? {
        return when (val geom = resolved.shape.geometry) {
            is ShapeGeometry.Box -> intersectBox(from, dir, resolved.transform, geom)
            is ShapeGeometry.Sphere -> intersectSphere(from, dir, resolved.transform, geom)
            is ShapeGeometry.Capsule -> intersectCapsule(from, dir, resolved.transform, geom)
            is ShapeGeometry.ConvexHull -> null  // TODO
        }
    }

    private fun intersectBox(from: Vec3D, dir: Vec3D, transform: Transform, geom: ShapeGeometry.Box): RayInfo? {
        // AABB intersection (simplified — без учёта ротации пока)
        val center = transform.translation
        val he = geom.halfExtents
        val min = center - he
        val max = center + he

        var tMin = Double.NEGATIVE_INFINITY
        var tMax = Double.POSITIVE_INFINITY

        for (i in 0..2) {
            val d = dir[i]; val o = from[i]
            val lo = min[i]; val hi = max[i]
            if (d == 0.0) {
                if (o < lo || o > hi) return null
            } else {
                val t1 = (lo - o) / d
                val t2 = (hi - o) / d
                tMin = maxOf(tMin, minOf(t1, t2))
                tMax = minOf(tMax, maxOf(t1, t2))
            }
        }
        if (tMin > tMax || tMax < 0) return null

        val inlet = from + dir * tMin
        val outlet = from + dir * tMax
        return RayInfo(inlet, tMin, null, outlet, tMax, null)
    }

    private fun intersectSphere(from: Vec3D, dir: Vec3D, transform: Transform, geom: ShapeGeometry.Sphere): RayInfo? {
        val oc = from - transform.translation
        val r = geom.radius.toDouble()
        val b = oc.dot(dir)
        val c = oc.dot(oc) - r * r
        val disc = b * b - c
        if (disc < 0) return null
        val sqrtDisc = Math.sqrt(disc)
        val t1 = -b - sqrtDisc
        val t2 = -b + sqrtDisc
        if (t2 < 0) return null
        val inlet = from + dir * t1
        val outlet = from + dir * t2
        return RayInfo(inlet, t1, null, outlet, t2, null)
    }

    private fun intersectCapsule(from: Vec3D, dir: Vec3D, transform: Transform, geom: ShapeGeometry.Capsule): RayInfo? {
        // TODO: полная реализация капсулы
        // Пока — упрощённая: тестируем как сферу радиуса + halfHeight
        val approxRadius = maxOf(geom.radius.toDouble(), geom.halfHeight.toDouble())
        val fakeGeom = ShapeGeometry.Sphere(approxRadius.toFloat())
        return intersectSphere(from, dir, transform, fakeGeom)
    }
}

private operator fun Vec3D.get(i: Int) = when (i) { 0 -> x; 1 -> y; else -> z }
