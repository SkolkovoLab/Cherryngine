package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class MinecartFurnaceMeta : AbstractMinecartMeta() {
    companion object : MinecartFurnaceMeta()

    val HAS_FUEL = index(MetadataEntry.Type.BOOLEAN, false)
}