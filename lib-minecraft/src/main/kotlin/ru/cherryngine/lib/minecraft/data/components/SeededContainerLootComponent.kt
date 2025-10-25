package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class SeededContainerLootComponent(
    val lootTable: String,
    val seed: Long
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            static("loot_table", CRC32CHasher.ofString(lootTable))
            static("seed", CRC32CHasher.ofLong(seed))
        }
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, SeededContainerLootComponent::lootTable,
            StreamCodec.LONG, SeededContainerLootComponent::seed,
            ::SeededContainerLootComponent
        )
    }
}