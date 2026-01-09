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
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.world.World

class ChunkMesher {
    companion object {
        fun createChunk(world: World, chunkPos: ChunkPos): BodyCreationSettings? {
            return generateChunkCollisionObject(world, chunkPos)
        }

        private fun generateChunkCollisionObject(
            world: World,
            chunkPos: ChunkPos,
        ): BodyCreationSettings? {
            val faces = getChunkFaces(world, chunkPos)
            if (faces.isEmpty()) return null

            val triangles = ArrayList<Triangle>()
            faces.forEach { it.addTris(triangles) }

            val shapeSettings = MeshShapeSettings(triangles)

            return BodyCreationSettings()
                .setMotionType(EMotionType.Static)
                .setObjectLayer(Layers.NON_MOVING)
                .setShape(shapeSettings.create().get())
        }

        private fun getChunkFaces(world: World, chunkPos: ChunkPos): List<Face> {
            var bottomY = world.dimensionType.minY + world.dimensionType.height
            var topY = world.dimensionType.minY

            // Determine real filled vertical range
            val minSection = world.dimensionType.minY / 16
            val maxSection = world.dimensionType.height / 16 + minSection
            for (sectionY in minSection until maxSection) {
                val sectionPos = SectionPos(chunkPos.x, sectionY, chunkPos.z)
                val section = world.getSectionOrNull(sectionPos) ?: continue
                if (section.hasOnlyAir()) continue

                val chunkBottom = sectionY * 16
                val chunkTop = chunkBottom + 16

                if (bottomY > chunkBottom) bottomY = chunkBottom
                if (topY < chunkTop) topY = chunkTop
            }

            val finalFaces = ArrayList<Face>()

            for (y in bottomY until topY) for (x in 0 until 16) for (z in 0 until 16) {
                val faces = getFaces(world, chunkPos, Vec3I(x, y, z)) ?: continue
                finalFaces += faces
            }

            return finalFaces
        }

        private fun getFaces(world: World, chunkPos: ChunkPos, relBlockPos: Vec3I): List<Face>? {
            val blockPos = Vec3I(
                relBlockPos.x + chunkPos.x * 16,
                relBlockPos.y,
                relBlockPos.z + chunkPos.z * 16
            )
            val block = world.getBlock(blockPos) ?: return null
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

                val neighbour = world.getBlock(blockPos + blockFace.vec)

                if (neighbour == null || !isFull(neighbour)) {
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
