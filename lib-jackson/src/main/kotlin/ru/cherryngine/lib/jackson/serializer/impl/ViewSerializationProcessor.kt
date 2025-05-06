package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import ru.cherryngine.lib.math.View

@Singleton
class ViewSerializationProcessor : JacksonSerializer<View>, JacksonDeserializer<View> {
    override fun serialize(value: View, gen: JsonGenerator) {
        gen.writeString("${value.yaw}, ${value.pitch}")
    }

    override fun deserialize(parser: JsonParser): View {
        val split = parser.text.replace(" ", "").split(",").map(String::toFloat)
        return View(split[0], split[1])
    }
}
