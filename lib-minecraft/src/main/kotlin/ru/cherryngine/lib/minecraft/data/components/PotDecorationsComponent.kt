package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.r2.Registries
import ru.cherryngine.lib.minecraft.registry.entries.Item

data class PotDecorationsComponent(
    val back: Item,
    val left: Item,
    val right: Item,
    val front: Item,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(
            CRC32CHasher.ofList(
                listOf(back, left, right, front)
                    .map { face -> CRC32CHasher.ofRegistryEntry(face) })
        )
    }

    companion object {
        val DEFAULT_ITEM = Registries.item["brick"]
        val EMPTY = PotDecorationsComponent(DEFAULT_ITEM, DEFAULT_ITEM, DEFAULT_ITEM, DEFAULT_ITEM)

        val STREAM_CODEC = StreamCodec.of(
            Registries.item.streamCodec, PotDecorationsComponent::back,
            Registries.item.streamCodec, PotDecorationsComponent::left,
            Registries.item.streamCodec, PotDecorationsComponent::right,
            Registries.item.streamCodec, PotDecorationsComponent::front,
            ::PotDecorationsComponent
        )
    }
}