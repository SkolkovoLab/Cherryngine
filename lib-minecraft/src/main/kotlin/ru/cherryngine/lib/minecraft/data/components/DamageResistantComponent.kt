package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

class DamageResistantComponent(
    val tagKey: String
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "types", Codec.STRING, DamageResistantComponent::tagKey,
            ::DamageResistantComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING, DamageResistantComponent::tagKey,
            ::DamageResistantComponent
        )
    }
}