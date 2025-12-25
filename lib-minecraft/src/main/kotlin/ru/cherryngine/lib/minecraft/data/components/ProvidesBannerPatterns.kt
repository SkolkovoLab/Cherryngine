package ru.cherryngine.lib.minecraft.data.components

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class ProvidesBannerPatterns(
    val identifier: Key
) : DataComponent() {

    companion object {
        val CODEC = Codec.KEY.transform(
            ::ProvidesBannerPatterns,
            ProvidesBannerPatterns::identifier
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.KEY, ProvidesBannerPatterns::identifier,
            ::ProvidesBannerPatterns
        )
    }
}