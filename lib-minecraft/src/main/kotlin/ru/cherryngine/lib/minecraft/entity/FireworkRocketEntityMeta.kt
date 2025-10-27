package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.item.ItemStack

@Suppress("PropertyName")
sealed class FireworkRocketEntityMeta : EntityMeta() {
    companion object : FireworkRocketEntityMeta()

    val ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    val SHOOTER_ENTITY_ID = index(MetadataEntry.Type.OPT_VAR_INT, null)
    val IS_SHOT_AT_ANGLE = index(MetadataEntry.Type.BOOLEAN, false)
}