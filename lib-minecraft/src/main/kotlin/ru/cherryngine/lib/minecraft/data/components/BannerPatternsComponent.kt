package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.BannerPattern

class BannerPatternsComponent(
    val layers: List<Layer>
) : DataComponent() {
    companion object {
        val CODEC = Layer.CODEC.list().transform(
            ::BannerPatternsComponent,
            BannerPatternsComponent::layers
        )
        val STREAM_CODEC = StreamCodec.of(
            Layer.STREAM_CODEC.list(), BannerPatternsComponent::layers,
            ::BannerPatternsComponent
        )
    }

    data class Layer(
        val pattern: BannerPattern,
        val color: DyeColor,
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "pattern", Registries.bannerPattern.keyCodec, Layer::pattern,
                "color", Codec.enum<DyeColor>(), Layer::color,
                ::Layer
            )
            val STREAM_CODEC = StreamCodec.of(
                RegistryStreamCodec(Registries.bannerPattern), Layer::pattern,
                EnumStreamCodec<DyeColor>(), Layer::color,
                ::Layer,
            )
        }
    }
}