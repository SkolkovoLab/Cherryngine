package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class OcelotMeta : AgeableMobMeta() {
    companion object : OcelotMeta()

    val IS_TRUSTING = index(MetadataEntry.Type.BOOLEAN, false)
}