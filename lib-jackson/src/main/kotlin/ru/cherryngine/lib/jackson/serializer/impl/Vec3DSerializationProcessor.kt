package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import ru.cherryngine.lib.math.Vec3D

@Singleton
class Vec3DSerializationProcessor : JacksonSerializer<Vec3D>, JacksonDeserializer<Vec3D> {
    override fun serialize(value: Vec3D, gen: JsonGenerator) {
        gen.writeString(vecToString(value))
    }

    override fun deserialize(parser: JsonParser): Vec3D {
        return vecFromString(parser.text)
    }

    companion object {
        fun vecFromString(s: String): Vec3D {
            val split = s.replace(" ", "").split(",").map(String::toDouble)
            return when (split.size) {
                3 -> Vec3D(split[0], split[1], split[2])
                1 -> Vec3D(split[0])
                else -> throw IllegalArgumentException()
            }
        }

        fun vecToString(vec: Vec3D): String {
            return "${vec.x}, ${vec.y}, ${vec.z}"
        }
    }
}
