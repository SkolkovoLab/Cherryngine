package ru.cherryngine.engine.physics.worldmesh

import com.github.stephengold.joltjni.BodyCreationSettings
import com.github.stephengold.joltjni.MeshShapeSettings
import com.github.stephengold.joltjni.Triangle
import com.github.stephengold.joltjni.enumerate.EMotionType
import ru.cherryngine.engine.physics.Layers
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.Direction
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.world.Chunk

class ChunkMesher {
    companion object {
        fun createChunk(chunk: Chunk, chunkPos: ChunkPos): BodyCreationSettings? {
            val dim = chunk.dimensionType
            val minY = dim.minY
            val maxY = dim.minY + dim.height
            return generateChunkCollisionObject(chunk, chunkPos, minY, maxY)
        }

        private fun generateChunkCollisionObject(
            chunk: Chunk,
            chunkPos: ChunkPos,
            minY: Int,
            maxY: Int,
        ): BodyCreationSettings? {
            val faces = getChunkFaces(chunk, chunkPos, minY, maxY)
            if (faces.isEmpty()) return null

            val triangles = ArrayList<Triangle>()
            faces.forEach { it.addTris(triangles) }

            val shapeSettings = MeshShapeSettings(triangles)

            return BodyCreationSettings()
                .setMotionType(EMotionType.Static)
                .setObjectLayer(Layers.NON_MOVING)
                .setShape(shapeSettings.create().get())
        }

        private fun getChunkFaces(chunk: Chunk, chunkPos: ChunkPos, minY: Int, maxY: Int): List<Face> {
            var bottomY = maxY
            var topY = minY

            // Determine real filled vertical range
            val sections = chunk.sections
            for (i in sections.indices) {
                val section = sections[i]
                if (section.hasOnlyAir()) continue

                val chunkBottom = minY + i * 16
                val chunkTop = chunkBottom + 16

                if (bottomY > chunkBottom) bottomY = chunkBottom
                if (topY < chunkTop) topY = chunkTop
            }

            val finalFaces = ArrayList<Face>()

            for (y in bottomY until topY) for (x in 0 until 16) for (z in 0 until 16) {
                val faces = getFaces(chunk, chunkPos, Vec3I(x, y, z)) ?: continue
                finalFaces += faces
            }

            return finalFaces
        }

        private fun getFaces(chunk: Chunk, chunkPos: ChunkPos, relBlockPos: Vec3I): List<Face>? {
            val block = chunk.getBlock(relBlockPos)
            if (block.isAir() || block.registryBlock.liquid) return null

            val faces = ArrayList<Face>()

            // TODO
//            val shape: Shape = block.registryBlock.collisionShape
            val relStart: Vec3D = Vec3D.ZERO
            val relEnd: Vec3D = Vec3D.ONE

            val blockX = chunkPos.x * 16 + relBlockPos.x
            val blockZ = chunkPos.z * 16 + relBlockPos.z

            for (blockFace in Direction.entries) {
                val face = Face(
                    blockFace,
                    if (blockFace == Direction.EAST) relEnd.x else relStart.x,
                    if (blockFace == Direction.UP) relEnd.y else relStart.y,
                    if (blockFace == Direction.SOUTH) relEnd.z else relStart.z,
                    if (blockFace == Direction.WEST) relStart.x else relEnd.x,
                    if (blockFace == Direction.DOWN) relStart.y else relEnd.y,
                    if (blockFace == Direction.NORTH) relStart.z else relEnd.z,
                    blockX,
                    relBlockPos.y,
                    blockZ
                )

                if (!face.isEdge()) {
                    faces += face
                    continue
                }

                val neighbour = chunk.getBlock(relBlockPos + blockFace.vec)

                if (!isFull(neighbour)) {
                    faces += face
                }
            }

            return faces
        }

        private fun isFull(block: Block): Boolean {
            if (block.isAir() || block.registryBlock.liquid) return false
            return true // TODO
        }
    }
}
