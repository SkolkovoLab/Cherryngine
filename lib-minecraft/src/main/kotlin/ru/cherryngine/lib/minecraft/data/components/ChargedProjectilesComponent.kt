package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ChargedProjectilesComponent(
    val projectiles: List<ItemStack>
) : DataComponent() {

    companion object {
        val CODEC = ItemStack.CODEC.list().transform(
            ::ChargedProjectilesComponent,
            ChargedProjectilesComponent::projectiles
        )
        val STREAM_CODEC = StreamCodec.of(
            ItemStack.STREAM_CODEC.list(), ChargedProjectilesComponent::projectiles,
            ::ChargedProjectilesComponent
        )
    }
}