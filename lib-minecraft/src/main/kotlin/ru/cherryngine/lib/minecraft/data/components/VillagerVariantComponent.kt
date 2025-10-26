package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.protocol.types.VillagerData
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class VillagerVariantComponent(
    val type: VillagerData.Type
) : DataComponent() {
    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<VillagerData.Type>(), VillagerVariantComponent::type,
            ::VillagerVariantComponent
        )
    }
}