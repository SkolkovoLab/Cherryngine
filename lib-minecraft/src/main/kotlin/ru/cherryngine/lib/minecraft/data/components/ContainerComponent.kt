package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class ContainerComponent(
    val items: List<ItemStack>
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            StructCodec.INLINE, Codec.INT, MaxDamageComponent::maxDamage,
            ::MaxDamageComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC.list(), ContainerComponent::items,
            ::ContainerComponent
        )
    }
}