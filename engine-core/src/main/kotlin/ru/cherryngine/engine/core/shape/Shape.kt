package ru.cherryngine.engine.core.shape

import ru.cherryngine.lib.math.Transform

interface Shape {
    val geometry: ShapeGeometry
    val getTransform: () -> Transform      // лямбда над реальным источником
}
