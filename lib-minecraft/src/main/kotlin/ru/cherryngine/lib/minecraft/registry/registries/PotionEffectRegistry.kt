package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.extentions.fromRGBInt
import ru.cherryngine.lib.minecraft.extentions.getPackedInt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.CustomColor

object PotionEffectRegistry : DataDrivenRegistry<PotionEffect>() {
    override val identifier: String = "minecraft:potion_effect"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class PotionEffect(
    val identifier: String,
    val name: String,
    val type: Type,
    val isInstant: Boolean,
    @Serializable(with = CustomColorIntSerializer::class)
    val color: CustomColor,
    ) : RegistryEntry {

    enum class Type {
        BENEFICIAL,
        HARMFUL,
        NEUTRAL
    }

    override fun getProtocolId(): Int {
        return PotionEffectRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
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