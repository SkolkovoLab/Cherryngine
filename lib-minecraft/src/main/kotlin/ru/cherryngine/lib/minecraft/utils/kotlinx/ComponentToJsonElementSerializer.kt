package ru.cherryngine.lib.minecraft.utils.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer

object ComponentToJsonElementSerializer : KSerializer<Component> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Component")

    override fun deserialize(decoder: Decoder): Component {
        require(decoder is JsonDecoder)
        val jsonElement = decoder.decodeJsonElement()
        val jsonString = Json.encodeToString(jsonElement)
        return JSONComponentSerializer.json().deserialize(jsonString)
    }

    override fun serialize(encoder: Encoder, value: Component) {
        require(encoder is JsonEncoder)
        val jsonString = JSONComponentSerializer.json().serialize(value)
        val jsonElement: JsonElement = Json.parseToJsonElement(jsonString)
        return encoder.encodeJsonElement(jsonElement)
    }
}