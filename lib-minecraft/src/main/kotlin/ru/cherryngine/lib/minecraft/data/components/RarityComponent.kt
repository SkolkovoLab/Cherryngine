package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.protocol.types.ItemRarity
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class RarityComponent(
    val rarity: ItemRarity
) : DataComponent(true) {

    companion object {
        val CODEC = Codec.enum<ItemRarity>().transform(
            ::RarityComponent,
            RarityComponent::rarity
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<ItemRarity>(), RarityComponent::rarity,
            ::RarityComponent
        )
    }
}