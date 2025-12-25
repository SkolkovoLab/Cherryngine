package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.ComponentStreamCodecs
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class LoreComponent(
    val lore: List<Component>
) : DataComponent() {

    companion object {
        val CODEC = ComponentCodec.list().transform(
            ::LoreComponent,
            LoreComponent::lore
        )
        val STREAM_CODEC = StreamCodec.of(
            ComponentStreamCodecs.NBT.list(), LoreComponent::lore,
            ::LoreComponent
        )
    }
}