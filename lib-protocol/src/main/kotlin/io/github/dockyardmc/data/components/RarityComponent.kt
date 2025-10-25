package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.protocol.types.ItemRarity
import io.github.dockyardmc.tide.stream.EnumStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec

data class RarityComponent(
    val rarity: ItemRarity
) : DataComponent(true) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(rarity))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<ItemRarity>(), RarityComponent::rarity,
            ::RarityComponent
        )
    }
}