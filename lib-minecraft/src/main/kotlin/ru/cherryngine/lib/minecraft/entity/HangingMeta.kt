package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.Direction

@Suppress("PropertyName")
sealed class HangingMeta : EntityMeta() {
    companion object : HangingMeta()

    val DIRECTION = index(MetadataEntry.Type.DIRECTION, Direction.SOUTH)
}