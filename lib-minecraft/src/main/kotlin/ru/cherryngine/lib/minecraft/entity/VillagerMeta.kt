package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.VillagerData

@Suppress("PropertyName")
sealed class VillagerMeta : AbstractVillagerMeta() {
    companion object : VillagerMeta()

    val VARIANT = index(MetadataEntry.Type.VILLAGER_DATA, VillagerData.DEFAULT)
}