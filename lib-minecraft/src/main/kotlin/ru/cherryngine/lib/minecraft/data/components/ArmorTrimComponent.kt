package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.TrimMaterial
import ru.cherryngine.lib.minecraft.registry.types.TrimPattern

data class ArmorTrimComponent(
    val material: TrimMaterial,
    val pattern: TrimPattern,
) : DataComponent() {
    companion object {
        val CODEC = StructCodec.of(
            "material", Registries.trimMaterial.keyCodec, ArmorTrimComponent::material,
            "pattern", Registries.trimPattern.keyCodec, ArmorTrimComponent::pattern,
            ::ArmorTrimComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            Registries.trimMaterial.streamCodec, ArmorTrimComponent::material,
            Registries.trimPattern.streamCodec, ArmorTrimComponent::pattern,
            ::ArmorTrimComponent
        )
    }
}