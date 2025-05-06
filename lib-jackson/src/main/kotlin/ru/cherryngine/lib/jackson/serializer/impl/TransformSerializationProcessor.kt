package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.rotation.QRot

@Singleton
class TransformSerializationProcessor : JacksonSerializer<Transform>, JacksonDeserializer<Transform> {
    override fun serialize(value: Transform, gen: JsonGenerator) {
        val t = Vec3DSerializationProcessor.vecToString(value.t)
        val r = QRotSerializationProcessor.qRotToString(value.r)
        val s = Vec3DSerializationProcessor.vecToString(value.s)
        gen.writeString("$t | $r | $s")
    }

    override fun deserialize(parser: JsonParser): Transform {
        val split = parser.text.replace(" ", "").split("|")

        val translation = Vec3DSerializationProcessor.vecFromString(split[0])

        val rotation = if (split.size >= 2) {
            QRotSerializationProcessor.qRotFromString(split[1])
        } else QRot.IDENTITY

        val scale = if (split.size >= 3) {
            Vec3DSerializationProcessor.vecFromString(split[2])
        } else Vec3D.ONE

        return Transform(translation, rotation, scale)
    }
}
