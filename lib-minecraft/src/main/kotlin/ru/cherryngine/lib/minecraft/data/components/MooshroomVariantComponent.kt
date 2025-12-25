package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.MooshroomMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class MooshroomVariantComponent(
    val variant: MooshroomMeta.Variant,
) : DataComponent() {
    companion object {
        val CODEC = Codec.enum<MooshroomMeta.Variant>().transform(
            ::MooshroomVariantComponent,
            MooshroomVariantComponent::variant
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<MooshroomMeta.Variant>(), MooshroomVariantComponent::variant,
            ::MooshroomVariantComponent
        )
    }
}