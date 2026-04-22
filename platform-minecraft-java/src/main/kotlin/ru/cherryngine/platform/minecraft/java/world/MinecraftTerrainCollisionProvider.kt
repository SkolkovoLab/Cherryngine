package ru.cherryngine.platform.minecraft.java.world

import jakarta.inject.Singleton
import net.minestom.server.collision.ShapeImpl
import ru.cherryngine.engine.core.world.ServerWorld
import ru.cherryngine.engine.core.world.TerrainCollisionProvider
import ru.cherryngine.lib.math.Cuboid
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.platform.minecraft.java.MinecraftServerWorld
import ru.cherryngine.platform.minecraft.java.utils.cuboid

/**
 * Реализация [TerrainCollisionProvider] поверх [MinecraftServerWorld]:
 * коллизии берутся из `Block.registry().collisionShape()` (cast в `ShapeImpl`
 * — все vanilla-shape'ы это `ShapeImpl`). Если shape пустой или не `ShapeImpl`,
 * для солидного блока откатываемся в единичный куб.
 *
 * Свойства поверхности — частный набор vanilla-льда/слайма.
 */
@Singleton
class MinecraftTerrainCollisionProvider : TerrainCollisionProvider<MinecraftServerWorld> {

    override fun canHandle(target: ServerWorld): Boolean = target is MinecraftServerWorld

    override fun getCollisionCuboids(
        pos: Vec3I,
        world: MinecraftServerWorld,
        contextIDs: Set<String>,
    ): List<Cuboid> {
        val block = world.getBlock(pos, contextIDs)
        if (block.isAir) return emptyList()
        val reg = block.registry() ?: return emptyList()
        val shape = reg.collisionShape()
        if (shape is ShapeImpl) {
            val boxes = shape.boundingBoxes()
            if (boxes.isNotEmpty()) {
                return boxes.map(net.minestom.server.collision.BoundingBox::cuboid)
            }
        }
        return if (reg.isSolid) listOf(UNIT_CUBE) else emptyList()
    }

    override fun getSurfaceProperties(
        pos: Vec3I,
        world: MinecraftServerWorld,
        contextIDs: Set<String>,
    ): TerrainCollisionProvider.SurfaceProperties {
        return when (world.getBlock(pos, contextIDs).key().value()) {
            "ice", "packed_ice", "blue_ice" -> TerrainCollisionProvider.SurfaceProperties(friction = 0.1f)
            "slime_block" -> TerrainCollisionProvider.SurfaceProperties(friction = 0.8f, restitution = 0.8f)
            else -> TerrainCollisionProvider.SurfaceProperties()
        }
    }

    companion object {
        private val UNIT_CUBE = Cuboid(Vec3D(0.0, 0.0, 0.0), Vec3D(1.0, 1.0, 1.0))
    }
}
