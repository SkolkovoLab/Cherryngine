package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.CatVariant

data class CatVariantComponent(
    val variant: CatVariant,
) : DataComponent() {

    companion object {
        val CODEC = Registries.catVariant.keyCodec.transform(
            ::CatVariantComponent,
            CatVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            Registries.catVariant.streamCodec, CatVariantComponent::variant,
            ::CatVariantComponent
        )
    }
}