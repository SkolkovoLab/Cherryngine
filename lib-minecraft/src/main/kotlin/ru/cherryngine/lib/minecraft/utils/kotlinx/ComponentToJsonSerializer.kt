package ru.cherryngine.lib.minecraft.utils.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

object ComponentToJsonSerializer : KSerializer<Component> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("component", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Component {
        val string = decoder.decodeString()
        return JSONComponentSerializer.json().deserialize(string)
    }

    override fun serialize(encoder: Encoder, value: Component) {
        val string = JSONComponentSerializer.json().serialize(value)
        return encoder.encodeString(string)
    }

}