package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import ru.cherryngine.lib.math.Cuboid

@Singleton
class CuboidSerializerProcessor : JacksonSerializer<Cuboid>, JacksonDeserializer<Cuboid> {
    override fun serialize(value: Cuboid, gen: JsonGenerator) {
        val p1 = Vec3DSerializationProcessor.vecToString(value.min)
        val p2 = Vec3DSerializationProcessor.vecToString(value.max)
        gen.writeString("$p1 | $p2")
    }

    override fun deserialize(parser: JsonParser): Cuboid {
        val split = parser.text.replace(" ", "").split("|")
        val p1 = Vec3DSerializationProcessor.vecFromString(split[0])
        val p2 = Vec3DSerializationProcessor.vecFromString(split[1])
        return Cuboid.fromTwoPoints(p1, p2)
    }
}
