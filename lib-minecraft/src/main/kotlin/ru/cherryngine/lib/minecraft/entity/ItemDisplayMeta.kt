package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.item.ItemStack

sealed class ItemDisplayMeta : DisplayMeta() {
    companion object : ItemDisplayMeta()

    val DISPLAYED_ITEM = index(MetadataEntry.Type.ITEM_STACK, ItemStack.AIR)
    val DISPLAY_TYPE = index<Byte, ItemDisplayType>(
        MetadataEntry.Type.BYTE,
        ItemDisplayType.NONE,
        ::fromIndex,
        ::byteIndex
    )

    enum class ItemDisplayType {
        NONE,
        THIRD_PERSON_LEFT_HAND,
        THIRD_PERSON_RIGHT_HAND,
        FIRST_PERSON_LEFT_HAND,
        FIRST_PERSON_RIGHT_HAND,
        HEAD,
        GUI,
        GROUND,
        FIXED;
    }
}