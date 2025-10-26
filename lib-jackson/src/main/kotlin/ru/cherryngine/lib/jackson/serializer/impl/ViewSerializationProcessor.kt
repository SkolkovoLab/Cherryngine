package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import ru.cherryngine.lib.math.YawPitch

@Singleton
class ViewSerializationProcessor : JacksonSerializer<YawPitch>, JacksonDeserializer<YawPitch> {
    override fun serialize(value: YawPitch, gen: JsonGenerator) {
        gen.writeString("${value.yaw}, ${value.pitch}")
    }

    override fun deserialize(parser: JsonParser): YawPitch {
        val split = parser.text.replace(" ", "").split(",").map(String::toFloat)
        return YawPitch(split[0], split[1])
    }
}
