package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AbstractVillagerMeta : AgeableMobMeta() {
    companion object : AbstractVillagerMeta()

    val HEAD_SHAKE_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
}