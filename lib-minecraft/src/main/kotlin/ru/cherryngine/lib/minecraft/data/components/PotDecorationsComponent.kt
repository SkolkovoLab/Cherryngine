package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.registry.Items
import ru.cherryngine.lib.minecraft.registry.registries.Item
import ru.cherryngine.lib.minecraft.registry.registries.ItemRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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