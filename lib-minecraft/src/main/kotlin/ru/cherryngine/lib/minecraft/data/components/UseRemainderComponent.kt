package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class UseRemainderComponent(
    val remained: ItemStack
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(remained.hashStruct().getHashed())
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC, UseRemainderComponent::remained,
            ::UseRemainderComponent
        )
    }
}