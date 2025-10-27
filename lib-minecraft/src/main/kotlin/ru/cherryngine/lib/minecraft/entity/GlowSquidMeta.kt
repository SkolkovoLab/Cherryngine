package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class GlowSquidMeta : AgeableMobMeta() {
    companion object : GlowSquidMeta()

    val DARK_TICKS_REMAINING = index(MetadataEntry.Type.VAR_INT, 0)
}