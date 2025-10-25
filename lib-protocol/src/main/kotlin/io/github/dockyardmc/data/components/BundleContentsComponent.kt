package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.item.ItemStack
import io.github.dockyardmc.tide.stream.StreamCodec

class BundleContentsComponent(
    val contents: List<ItemStack>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        val finalHash = mutableListOf<Int>()
        contents.forEach { item ->
            finalHash.add(item.hashStruct().getHashed())
        }
        return StaticHash(CRC32CHasher.ofList(finalHash))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC.list(), BundleContentsComponent::contents,
            ::BundleContentsComponent
        )
    }
}