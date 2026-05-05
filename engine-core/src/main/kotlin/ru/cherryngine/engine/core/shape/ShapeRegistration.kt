package ru.cherryngine.engine.core.shape

import java.util.UUID

class ShapeRegistration(
    private val shapeWorld: ShapeWorld,
    val id: UUID = UUID.randomUUID(),
) : AutoCloseable {
    override fun close() = shapeWorld.unregister(id)
}
