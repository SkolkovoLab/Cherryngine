package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.tide.stream.StreamCodec

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