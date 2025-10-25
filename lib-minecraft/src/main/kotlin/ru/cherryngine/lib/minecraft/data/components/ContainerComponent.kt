package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ContainerComponent(
    val items: List<ItemStack>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        val finalHash = mutableListOf<Int>()
        items.forEach { item ->
            finalHash.add(item.hashStruct().getHashed())
        }
        return StaticHash(CRC32CHasher.ofList(finalHash))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC.list(), ContainerComponent::items,
            ::ContainerComponent
        )
    }
}