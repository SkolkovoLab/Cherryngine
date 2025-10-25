package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.registry.Items
import io.github.dockyardmc.registry.registries.Item
import io.github.dockyardmc.registry.registries.ItemRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

data class PotDecorationsComponent(
    val back: Item,
    val left: Item,
    val right: Item,
    val front: Item
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofList(listOf(back, left, right, front).map { face -> CRC32CHasher.ofRegistryEntry(face) }))
    }

    companion object {
        val DEFAULT_ITEM = Items.BRICK
        val EMPTY = PotDecorationsComponent(DEFAULT_ITEM, DEFAULT_ITEM, DEFAULT_ITEM, DEFAULT_ITEM)

        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(ItemRegistry), PotDecorationsComponent::back,
            RegistryStreamCodec(ItemRegistry), PotDecorationsComponent::left,
            RegistryStreamCodec(ItemRegistry), PotDecorationsComponent::right,
            RegistryStreamCodec(ItemRegistry), PotDecorationsComponent::front,
            ::PotDecorationsComponent
        )
    }
}