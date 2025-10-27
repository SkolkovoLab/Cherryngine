package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.math.Vec3I

@Suppress("PropertyName")
sealed class FallingBlockMeta : EntityMeta() {
    companion object : FallingBlockMeta()

    val SPAWN_POSITION = index(MetadataEntry.Type.BLOCK_POSITION, Vec3I.ZERO)
}