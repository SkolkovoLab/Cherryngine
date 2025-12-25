package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ItemNameComponent(
    val itemName: Component
) : DataComponent() {

    companion object {
        val CODEC = ComponentCodec.transform(
            ::ItemNameComponent,
            ItemNameComponent::itemName
        )
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.NBT, ItemNameComponent::itemName,
            ::ItemNameComponent
        )
    }
}