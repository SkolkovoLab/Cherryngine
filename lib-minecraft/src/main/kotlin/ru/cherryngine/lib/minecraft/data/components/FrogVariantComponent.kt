package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.FrogVariant

class FrogVariantComponent(
    val variant: FrogVariant,
) : DynamicVariantComponent<FrogVariant>(variant, Registries.frogVariant) {
    companion object {
        val CODEC = Registries.frogVariant.keyCodec.transform(
            ::FrogVariantComponent,
            FrogVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.frogVariant.streamCodec, FrogVariantComponent::variant,
            ::FrogVariantComponent
        )
    }
}