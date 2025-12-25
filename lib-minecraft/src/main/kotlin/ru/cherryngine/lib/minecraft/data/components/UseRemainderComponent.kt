package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class UseRemainderComponent(
    val remained: ItemStack
) : DataComponent() {

    companion object {
        val CODEC = ItemStack.CODEC.transform(
            ::UseRemainderComponent,
            UseRemainderComponent::remained
        )
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC, UseRemainderComponent::remained,
            ::UseRemainderComponent
        )
    }
}