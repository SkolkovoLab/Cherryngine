package ru.cherryngine.lib.jackson.serializer.impl

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import jakarta.inject.Singleton
import ru.cherryngine.lib.jackson.serializer.JacksonDeserializer
import ru.cherryngine.lib.jackson.serializer.JacksonSerializer
import ru.cherryngine.lib.math.rotation.AxisAngleSequence
import ru.cherryngine.lib.math.rotation.AxisSequence
import ru.cherryngine.lib.math.rotation.QRot

@Singleton
class QRotSerializationProcessor : JacksonSerializer<QRot>, JacksonDeserializer<QRot> {
    override fun serialize(value: QRot, gen: JsonGenerator) {
        gen.writeString(qRotToString(value))
    }

    override fun deserialize(parser: JsonParser): QRot {
        return qRotFromString(parser.text)
    }

    companion object {
        fun qRotFromString(s: String): QRot {
            val split = s.replace(" ", "").split(",").map(String::toDouble)
            return when (split.size) {
                4 -> QRot.of(split[0], split[1], split[2], split[3])
                3 -> AxisAngleSequence(
                    AxisSequence.XYZ,
                    Math.toRadians(split[0]),
                    Math.toRadians(split[1]),
                    Math.toRadians(split[2]),
                ).toQRot()

                else -> throw IllegalArgumentException()
            }
        }

        fun qRotToString(qRot: QRot): String {
            return "${qRot.w}, ${qRot.x}, ${qRot.y}, ${qRot.z}"
        }
    }
}
