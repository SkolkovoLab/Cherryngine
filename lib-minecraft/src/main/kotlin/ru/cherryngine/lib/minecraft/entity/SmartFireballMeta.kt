package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.item.ItemStack

@Suppress("PropertyName")
sealed class SmartFireballMeta : EntityMeta() {
    companion object : SmartFireballMeta()

    val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
}