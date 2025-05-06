package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer

@Singleton
class RGBLikeSerializationProcessor : JacksonSerializer<RGBLike>, JacksonDeserializer<RGBLike> {
    override fun serialize(value: RGBLike, gen: JsonGenerator) {
        gen.writeString("${value.red()}, ${value.green()}, ${value.blue()}")
    }

    override fun deserialize(parser: JsonParser): RGBLike {
        val text = parser.readValueAs(String::class.java)
        val (r, g, b) = text.replace(" ", "").split(",")
        return TextColor.color(r.toInt(), g.toInt(), b.toInt())
    }
}