package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.math.Transform
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.rotation.QRot

@Singleton
class TransformSerializationProcessor : KSerializer<Transform> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Transform", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Transform) {
        val t = Vec3DSerializationProcessor.vecToString(value.t)
        val r = QRotSerializationProcessor.qRotToString(value.r)
        val s = Vec3DSerializationProcessor.vecToString(value.s)
        encoder.encodeString("$t | $r | $s")
    }

    override fun deserialize(decoder: Decoder): Transform {
        val split = decoder.decodeString().replace(" ", "").split("|")
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