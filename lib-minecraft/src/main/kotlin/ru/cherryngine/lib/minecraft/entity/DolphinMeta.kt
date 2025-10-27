package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.math.Vec3I

@Suppress("PropertyName")
sealed class DolphinMeta : MobMeta() {
    companion object : DolphinMeta()

    val TREASURE_POSITION = index(MetadataEntry.Type.BLOCK_POSITION, Vec3I.ZERO)
    val HAS_FISH = index(MetadataEntry.Type.BOOLEAN, false)
    val MOISTURE_LEVEL = index(MetadataEntry.Type.VAR_INT, 2400)
}