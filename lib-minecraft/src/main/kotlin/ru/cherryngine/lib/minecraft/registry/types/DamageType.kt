package ru.cherryngine.lib.minecraft.registry.types

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class DamageType(
    val exhaustion: Float,
    val messageId: String,
    val scaling: String,
) {
    companion object {
        val CODEC = StructCodec.of(
            "exhaustion", Codec.FLOAT, DamageType::exhaustion,
            "message_id", Codec.STRING, DamageType::messageId,
            "scaling", Codec.STRING, DamageType::scaling,
            ::DamageType
        )
    }
}