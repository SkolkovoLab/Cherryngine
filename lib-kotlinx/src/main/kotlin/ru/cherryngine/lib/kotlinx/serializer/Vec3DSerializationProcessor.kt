package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.math.Vec3D

@Singleton
class Vec3DSerializationProcessor : KSerializer<Vec3D> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Vec3D", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Vec3D) {
        encoder.encodeString(vecToString(value))
    }

    override fun deserialize(decoder: Decoder): Vec3D {
        return vecFromString(decoder.decodeString())
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
