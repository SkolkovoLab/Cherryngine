package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class IntangibleProjectileComponent : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(::IntangibleProjectileComponent)
        val STREAM_CODEC = StreamCodec.of(
            ::IntangibleProjectileComponent
        )
    }
}