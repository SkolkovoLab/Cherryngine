package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.color.Color

@Singleton
class ColorSerializationProcessor : KSerializer<Color> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeString("${value.red()}, ${value.green()}, ${value.blue()}")
    }

    override fun deserialize(decoder: Decoder): Color {
        val text = decoder.decodeString()
        val (r, g, b) = text.replace(" ", "").split(",").map { it.toInt() }
        return Color(r, g, b)
    }
}