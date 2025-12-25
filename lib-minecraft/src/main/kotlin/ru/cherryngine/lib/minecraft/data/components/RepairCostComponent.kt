package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class RepairCostComponent(
    val cost: Int
) : DataComponent() {

    companion object {
        val CODEC = Codec.INT.transform(
            ::RepairCostComponent,
            RepairCostComponent::cost
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.VAR_INT, RepairCostComponent::cost,
            ::RepairCostComponent
        )
    }
}