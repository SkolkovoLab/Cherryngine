package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.entity.SalmonMeta
import ru.cherryngine.lib.minecraft.network.stream_codec.EnumStreamCodec
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class SalmonSizeComponent(
    val size: SalmonMeta.Size,
) : DataComponent() {

    companion object {
        val CODEC = Codec.enum<SalmonMeta.Size>().transform(
            ::SalmonSizeComponent,
            SalmonSizeComponent::size
        )
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<SalmonMeta.Size>(), SalmonSizeComponent::size,
            ::SalmonSizeComponent
        )
    }
}