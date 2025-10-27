package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class GuardianMeta : MobMeta() {
    companion object : GuardianMeta()

    val IS_RETRACTING_SPIKES = index(MetadataEntry.Type.BOOLEAN, false)
    val TARGET_EID = index(MetadataEntry.Type.VAR_INT, 0)
}