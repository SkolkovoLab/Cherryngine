package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class UseCooldownComponent(
    val seconds: Float,
    val cooldownGroup: String? = null
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            static("seconds", CRC32CHasher.ofFloat(seconds))
            optional("cooldown_group", cooldownGroup, CRC32CHasher::ofString)
        }
    }

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