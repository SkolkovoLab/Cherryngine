package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class CustomNameComponent(
    val component: Component,
) : DataComponent() {

    companion object {
        val CODEC = ComponentCodec.transform(
            ::CustomNameComponent,
            CustomNameComponent::component
        )
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.NBT, CustomNameComponent::component,
            ::CustomNameComponent
        )
    }
}