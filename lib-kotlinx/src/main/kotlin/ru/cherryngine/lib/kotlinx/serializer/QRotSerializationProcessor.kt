package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.math.rotation.AxisAngleSequence
import ru.cherryngine.lib.math.rotation.AxisSequence
import ru.cherryngine.lib.math.rotation.QRot

@Singleton
class QRotSerializationProcessor : KSerializer<QRot> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("QRot", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: QRot) {
        encoder.encodeString(qRotToString(value))
    }

    override fun deserialize(decoder: Decoder): QRot {
        return qRotFromString(decoder.decodeString())
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
