package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.minecraft.extentions.fromRGBInt
import ru.cherryngine.lib.minecraft.extentions.getPackedInt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.CustomColor

@Serializable
data class PotionEffect(
    val identifier: String,
    val translationKey: String,
    @Serializable(with = CustomColorIntSerializer::class)
    val color: CustomColor,
    val instantaneous: Boolean,
) : RegistryEntry {

    override fun getEntryIdentifier(): String {
        return identifier
    }

    object CustomColorIntSerializer : KSerializer<CustomColor> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("CustomColorInt", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: CustomColor) {
            encoder.encodeInt(value.getPackedInt())
        }

        override fun deserialize(decoder: Decoder): CustomColor {
            return CustomColor.fromRGBInt(decoder.decodeInt())
        }
    }
}