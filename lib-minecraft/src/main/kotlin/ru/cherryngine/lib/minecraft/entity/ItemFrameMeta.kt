package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.item.ItemStack

@Suppress("PropertyName")
sealed class ItemFrameMeta : HangingMeta() {
    companion object : ItemFrameMeta()

    val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    val ROTATION = index(MetadataEntry.Type.VAR_INT, 0)
}