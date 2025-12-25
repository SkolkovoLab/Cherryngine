package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.Item

class RepairableComponent(
    val materials: List<Item>,
) : DataComponent() {

    companion object {
        val CODEC = Registries.item.keyCodec.list().transform(
            ::RepairableComponent,
            RepairableComponent::materials
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.item.streamCodec.list(), RepairableComponent::materials,
            ::RepairableComponent
        )
    }
}