package ru.cherryngine.lib.minecraft.utils.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import ru.cherryngine.lib.minecraft.utils.CustomColor

// To make sure it has "color": Hex in the json
object CustomColorHexSerializer : KSerializer<CustomColor> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("CustomColor", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: CustomColor) {
        encoder.encodeString(value.toHex())
    }

    override fun deserialize(decoder: Decoder): CustomColor {
        return CustomColor.Companion.fromHex(decoder.decodeString())
    }
}