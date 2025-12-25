package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class SeededContainerLootComponent(
    val lootTable: String,
    val seed: Long
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "loot_table", Codec.STRING, SeededContainerLootComponent::lootTable,
            "seed", Codec.LONG, SeededContainerLootComponent::seed,
            ::SeededContainerLootComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, SeededContainerLootComponent::lootTable,
            StreamCodec.LONG, SeededContainerLootComponent::seed,
            ::SeededContainerLootComponent
        )
    }
}