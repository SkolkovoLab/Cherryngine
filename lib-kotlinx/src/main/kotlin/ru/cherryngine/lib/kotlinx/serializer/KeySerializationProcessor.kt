package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.key.Key

@Singleton
class KeySerializationProcessor : KSerializer<Key> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Key", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Key) {
        encoder.encodeString(value.asString())
    }

    override fun deserialize(decoder: Decoder): Key {
        return Key.key(decoder.decodeString())
    }
}