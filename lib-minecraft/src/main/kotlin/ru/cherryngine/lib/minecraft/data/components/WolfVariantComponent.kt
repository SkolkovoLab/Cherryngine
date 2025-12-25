package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.WolfVariant

class WolfVariantComponent(
    val variant: WolfVariant,
) : DynamicVariantComponent<WolfVariant>(variant, Registries.wolfVariant) {
    companion object {
        val CODEC = Registries.wolfVariant.keyCodec.transform(
            ::WolfVariantComponent,
            WolfVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.wolfVariant.streamCodec, WolfVariantComponent::variant,
            ::WolfVariantComponent
        )
    }
}