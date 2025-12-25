package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.ChickenVariant

class ChickenVariantComponent(
    val variant: ChickenVariant,
) : DynamicVariantComponent<ChickenVariant>(variant, Registries.chickenVariant) {

    companion object {
        val CODEC = Registries.chickenVariant.keyCodec.transform(
            ::ChickenVariantComponent,
            ChickenVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.chickenVariant.streamCodec, ChickenVariantComponent::variant,
            ::ChickenVariantComponent
        )
    }
}