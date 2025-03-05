package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.math.Cuboid

@Singleton
class CuboidSerializerProcessor : KSerializer<Cuboid> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Cuboid", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Cuboid) {
        val p1 = Vec3DSerializationProcessor.vecToString(value.min)
        val p2 = Vec3DSerializationProcessor.vecToString(value.max)
        encoder.encodeString("$p1 | $p2")
    }

    override fun deserialize(decoder: Decoder): Cuboid {
        val text = decoder.decodeString()
        val split = text.replace(" ", "").split("|")
        val p1 = Vec3DSerializationProcessor.vecFromString(split[0])
        val p2 = Vec3DSerializationProcessor.vecFromString(split[1])
        return Cuboid.fromTwoPoints(p1, p2)
    }
}