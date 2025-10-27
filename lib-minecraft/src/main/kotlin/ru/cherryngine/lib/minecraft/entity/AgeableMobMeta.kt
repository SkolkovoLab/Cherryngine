package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AgeableMobMeta : MobMeta() {
    companion object : AgeableMobMeta()

    val IS_BABY = index(MetadataEntry.Type.BOOLEAN, false)
}