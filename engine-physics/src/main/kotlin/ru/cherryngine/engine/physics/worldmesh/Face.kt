package ru.cherryngine.engine.physics.worldmesh

import com.github.stephengold.joltjni.Triangle
import com.github.stephengold.joltjni.Vec3
import ru.cherryngine.lib.minecraft.network.protocol.types.Direction

data class Face(
    val blockFace: Direction,
    val minX: Float,
    val minY: Float,
    val minZ: Float,
    val maxX: Float,
    val maxY: Float,
    val maxZ: Float,
    val blockX: Int,
    val blockY: Int,
    val blockZ: Int,
) {
    constructor(
        blockFace: Direction,
        minX: Double,
        minY: Double,
        minZ: Double,
        maxX: Double,
        maxY: Double,
        maxZ: Double,
        blockX: Int,
        blockY: Int,
        blockZ: Int,
    ) : this(
        blockFace,
        minX.toFloat(),
        minY.toFloat(),
        minZ.toFloat(),
        maxX.toFloat(),
        maxY.toFloat(),
        maxZ.toFloat(),
        blockX,
        blockY,
        blockZ
    )

    fun isEdge(): Boolean =
        when (blockFace) {
            Direction.DOWN -> minY == 0.0f
            Direction.UP -> maxY == 1.0f
            Direction.NORTH -> minZ == 0.0f
            Direction.SOUTH -> maxZ == 1.0f
            Direction.WEST -> minX == 0.0f
            Direction.EAST -> maxX == 1.0f
        }

    fun addTris(triangles: MutableList<Triangle>) {
        val (p1, p2, p3, p4) = when (blockFace) {

            Direction.UP -> listOf(
                Vec3(minX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, maxZ + blockZ),
                Vec3(minX + blockX, maxY + blockY, maxZ + blockZ)
            )

            Direction.DOWN -> listOf(
                Vec3(maxX + blockX, maxY + blockY, maxZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(minX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(minX + blockX, maxY + blockY, maxZ + blockZ)
            )

            Direction.WEST -> listOf(
                Vec3(maxX + blockX, minY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, maxZ + blockZ),
                Vec3(maxX + blockX, minY + blockY, maxZ + blockZ)
            )

            Direction.EAST -> listOf(
                Vec3(maxX + blockX, maxY + blockY, maxZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, minY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, minY + blockY, maxZ + blockZ)
            )

            Direction.SOUTH -> listOf(
                Vec3(maxX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, minY + blockY, minZ + blockZ),
                Vec3(minX + blockX, minY + blockY, minZ + blockZ),
                Vec3(minX + blockX, maxY + blockY, minZ + blockZ)
            )

            Direction.NORTH -> listOf(
                Vec3(minX + blockX, minY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, minY + blockY, minZ + blockZ),
                Vec3(maxX + blockX, maxY + blockY, minZ + blockZ),
                Vec3(minX + blockX, maxY + blockY, minZ + blockZ)
            )
        }

        triangles += Triangle(p3, p2, p1)
        triangles += Triangle(p1, p4, p3)
    }
}
