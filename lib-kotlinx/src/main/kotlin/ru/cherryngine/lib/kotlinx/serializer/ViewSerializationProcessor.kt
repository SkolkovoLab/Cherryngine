package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.math.View

@Singleton
class ViewSerializationProcessor : KSerializer<View> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("View", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: View) {
        encoder.encodeString("${value.yaw}, ${value.pitch}")
    }

    override fun deserialize(decoder: Decoder): View {
        val split = decoder.decodeString().replace(" ", "").split(",").map(String::toFloat)
        return View(split[0], split[1])
    }
}