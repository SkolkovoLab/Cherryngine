package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.Items
import ru.cherryngine.lib.minecraft.registry.types.Item

data class PotDecorationsComponent(
    val back: Item,
    val left: Item,
    val right: Item,
    val front: Item,
) : DataComponent() {

    companion object {
        val DEFAULT_ITEM = Registries.item[Items.BRICK].value
        val EMPTY = PotDecorationsComponent(DEFAULT_ITEM, DEFAULT_ITEM, DEFAULT_ITEM, DEFAULT_ITEM)

        val CODEC = Registries.item.keyCodec.list().transform(
            { PotDecorationsComponent(it[0], it[1], it[2], it[3]) },
            { listOf(it.back, it.left, it.right, it.front) }
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.item.streamCodec, PotDecorationsComponent::back,
            Registries.item.streamCodec, PotDecorationsComponent::left,
            Registries.item.streamCodec, PotDecorationsComponent::right,
            Registries.item.streamCodec, PotDecorationsComponent::front,
            ::PotDecorationsComponent
        )
    }
}