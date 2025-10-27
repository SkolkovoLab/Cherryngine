package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.VillagerData

@Suppress("PropertyName")
sealed class ZombieVillagerMeta : MobMeta() {
    companion object : ZombieVillagerMeta()

    val IS_CONVERTING = index(MetadataEntry.Type.BOOLEAN, false)
    val VILLAGER_DATA = index(MetadataEntry.Type.VILLAGER_DATA, VillagerData.DEFAULT)
}