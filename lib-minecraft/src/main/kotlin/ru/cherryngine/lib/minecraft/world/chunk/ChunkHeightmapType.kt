package ru.cherryngine.lib.minecraft.world.chunk

import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import java.util.function.Predicate

/**
 * Тонкая обёртка над `Heightmap.Type` из Minestom с предикатом для пересчёта.
 * Индексы `toMinestom()` совпадают с перечислением Minestom.
 */
enum class ChunkHeightmapType(val predicate: Predicate<Block>) {
    WORLD_SURFACE({ !it.isAir }),
    MOTION_BLOCKING({ val r = it.registry()!!; r.isSolid || r.isLiquid }),
    MOTION_BLOCKING_NO_LEAVES({ it.registry()!!.isSolid && !it.key().value().endsWith("_leaves") });

    fun toMinestom(): Heightmap.Type = when (this) {
        WORLD_SURFACE -> Heightmap.Type.WORLD_SURFACE
        MOTION_BLOCKING -> Heightmap.Type.MOTION_BLOCKING
        MOTION_BLOCKING_NO_LEAVES -> Heightmap.Type.MOTION_BLOCKING_NO_LEAVES
    }

    companion object {
        fun fromMinestom(type: Heightmap.Type): ChunkHeightmapType? = when (type) {
            Heightmap.Type.WORLD_SURFACE -> WORLD_SURFACE
            Heightmap.Type.MOTION_BLOCKING -> MOTION_BLOCKING
            Heightmap.Type.MOTION_BLOCKING_NO_LEAVES -> MOTION_BLOCKING_NO_LEAVES
            else -> null
        }
    }
}
