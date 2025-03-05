package ru.cherryngine.lib.kotlinx.serializer

import jakarta.inject.Singleton
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.item.Material
import net.minestom.server.utils.NamespaceID

@Singleton
class MaterialSerializationProcessor : KSerializer<Material> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Material", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Material) {
        encoder.encodeString(value.key().value().uppercase())
    }

    override fun deserialize(decoder: Decoder): Material {
        return Material.fromNamespaceId(NamespaceID.from(decoder.decodeString().lowercase()))!!
    }
}