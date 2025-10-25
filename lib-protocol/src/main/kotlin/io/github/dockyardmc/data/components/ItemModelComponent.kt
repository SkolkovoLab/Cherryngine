package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.tide.stream.StreamCodec

class ItemModelComponent(
    val itemModel: String
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofString(itemModel))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, ItemModelComponent::itemModel,
            ::ItemModelComponent
        )
    }
}