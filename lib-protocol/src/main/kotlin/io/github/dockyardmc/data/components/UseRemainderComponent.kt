package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.tide.stream.StreamCodec

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