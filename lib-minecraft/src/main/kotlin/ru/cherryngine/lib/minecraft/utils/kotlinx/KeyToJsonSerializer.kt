package ru.cherryngine.lib.minecraft.utils.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key

object KeyToJsonSerializer : KSerializer<Key> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("key", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Key {
        return Key.key(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Key) {
        return encoder.encodeString(value.asString())
    }
}