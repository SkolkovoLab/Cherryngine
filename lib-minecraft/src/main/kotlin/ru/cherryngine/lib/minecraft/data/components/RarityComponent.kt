package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.types.ItemRarity
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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