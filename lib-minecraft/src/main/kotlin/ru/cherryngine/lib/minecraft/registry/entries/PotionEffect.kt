package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.color.asARGB
import ru.cherryngine.lib.minecraft.utils.color.rgbLikeOf

@Serializable
data class PotionEffect(
    val identifier: String,
    val translationKey: String,
    @Serializable(with = CustomColorIntSerializer::class)
    val color: RGBLike,
    val instantaneous: Boolean,
) : RegistryEntry {

    override fun getEntryIdentifier(): String {
        return identifier
    }

    object CustomColorIntSerializer : KSerializer<RGBLike> {
        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("CustomColorInt", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: RGBLike) {
            encoder.encodeInt(value.asARGB())
        }

        override fun deserialize(decoder: Decoder): RGBLike {
            return rgbLikeOf(decoder.decodeInt())
        }
    }
}