package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.item.ItemStack

@Suppress("PropertyName")
sealed class OminousItemSpawnerMeta : EntityMeta() {
    companion object : OminousItemSpawnerMeta()

    val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
}