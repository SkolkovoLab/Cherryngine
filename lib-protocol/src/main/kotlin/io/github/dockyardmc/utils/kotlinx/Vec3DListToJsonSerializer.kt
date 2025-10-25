package io.github.dockyardmc.utils.kotlinx

import ru.cherryngine.lib.math.Vec3D
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object Vec3DListToJsonSerializer : KSerializer<List<Vec3D>> {
    private val delegateSerializer = ListSerializer(Vec3DToJsonSerializer)

    override val descriptor: SerialDescriptor = delegateSerializer.descriptor

    override fun serialize(encoder: Encoder, value: List<Vec3D>) {
        delegateSerializer.serialize(encoder, value)
    }

    override fun deserialize(decoder: Decoder): List<Vec3D> {
        return delegateSerializer.deserialize(decoder)
    }
}