package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.CowVariant

class CowVariantComponent(
    val variant: CowVariant,
) : DynamicVariantComponent<CowVariant>(variant, Registries.cowVariant) {

    companion object {
        val CODEC = Registries.cowVariant.keyCodec.transform(
            ::CowVariantComponent,
            CowVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.cowVariant.streamCodec, CowVariantComponent::variant,
            ::CowVariantComponent
        )
    }
}