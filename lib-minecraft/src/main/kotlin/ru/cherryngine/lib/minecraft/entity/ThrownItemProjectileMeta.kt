package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.item.ItemStack

@Suppress("PropertyName")
sealed class ThrownItemProjectileMeta : EntityMeta() {
    companion object : ThrownItemProjectileMeta()

    val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.Companion.AIR)
}