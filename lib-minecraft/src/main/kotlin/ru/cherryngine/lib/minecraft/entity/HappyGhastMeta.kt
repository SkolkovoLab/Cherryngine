package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class HappyGhastMeta : AgeableMobMeta() {
    companion object : HappyGhastMeta()

    val IS_LEASH_HOLDER = index(MetadataEntry.Type.BOOLEAN, false)
    val STAYS_STILL = index(MetadataEntry.Type.BOOLEAN, false)
}