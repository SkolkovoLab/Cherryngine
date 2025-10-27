package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class HoglinMeta : AgeableMobMeta() {
    companion object : HoglinMeta()

    val IMMUNE_ZOMBIFICATION = index(MetadataEntry.Type.BOOLEAN, false)
}