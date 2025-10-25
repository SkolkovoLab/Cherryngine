package io.github.dockyardmc.utils.kotlinx

import ru.cherryngine.lib.math.Vec3D
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure

object Vec3DToJsonSerializer : KSerializer<Vec3D> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Vec3D") {
        element<Double>("x")
        element<Double>("y")
        element<Double>("z")
    }

    override fun serialize(encoder: Encoder, value: Vec3D) {
        encoder.encodeStructure(descriptor) {
            encodeDoubleElement(descriptor, 0, value.x)
            encodeDoubleElement(descriptor, 1, value.y)
            encodeDoubleElement(descriptor, 2, value.z)
        }
    }

    override fun deserialize(decoder: Decoder): Vec3D {
        return decoder.decodeStructure(descriptor) {
            var x = 0.0
            var y = 0.0
            var z = 0.0

            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0 -> x = decodeDoubleElement(descriptor, 0)
                    1 -> y = decodeDoubleElement(descriptor, 1)
                    2 -> z = decodeDoubleElement(descriptor, 2)
                    CompositeDecoder.Companion.DECODE_DONE -> break
                    else -> throw SerializationException("Unexpected index $index")
                }
            }

            Vec3D(x, y, z)
        }
    }
}