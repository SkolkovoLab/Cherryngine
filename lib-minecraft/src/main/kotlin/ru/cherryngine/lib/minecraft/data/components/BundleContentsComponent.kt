package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class BundleContentsComponent(
    val contents: List<ItemStack>
) : DataComponent() {
    companion object {
        val CODEC = ItemStack.CODEC.list().transform(
            ::BundleContentsComponent,
            BundleContentsComponent::contents
        )
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC.list(), BundleContentsComponent::contents,
            ::BundleContentsComponent
        )
    }
}