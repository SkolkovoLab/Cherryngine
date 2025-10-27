package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.Direction
import ru.cherryngine.lib.minecraft.protocol.types.DyeColor

@Suppress("PropertyName")
sealed class ShulkerMeta : MobMeta() {
    companion object : ShulkerMeta()

    val ATTACH_FACE = index(MetadataEntry.Type.DIRECTION, Direction.DOWN)
    val SHIELD_HEIGHT = index(MetadataEntry.Type.BYTE, 0)
    val COLOR = index(
        MetadataEntry.Type.BYTE,
        null,
        { DyeColor.entries.getOrNull(it.toInt()) },
        { (it?.ordinal ?: 16).toByte() }
    )
}