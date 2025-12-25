package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec

data class UseCooldownComponent(
    val seconds: Float,
    val cooldownGroup: String? = null
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "seconds", Codec.FLOAT, UseCooldownComponent::seconds,
            "cooldown_group", Codec.STRING.optional(), UseCooldownComponent::cooldownGroup,
            ::UseCooldownComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.FLOAT, UseCooldownComponent::seconds,
            StreamCodec.STRING.optional(), UseCooldownComponent::cooldownGroup,
            ::UseCooldownComponent
        )
    }
}