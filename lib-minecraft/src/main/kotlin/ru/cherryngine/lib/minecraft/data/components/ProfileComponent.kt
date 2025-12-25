package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import java.util.*

data class ProfileComponent(
    val name: String?,
    val uuid: UUID?,
    val properties: List<Property>
) : DataComponent() {

    companion object {
        val CODEC = StructCodec.of(
            "name", Codec.STRING.optional(), ProfileComponent::name,
            "uuid", Codec.UUID.optional(), ProfileComponent::uuid,
            "properties", Property.CODEC.list().default(emptyList()), ProfileComponent::properties,
            ::ProfileComponent
        )
        val STREAM_CODEC = StreamCodec.of(
            StreamCodec.STRING.optional(), ProfileComponent::name,
            StreamCodec.UUID.optional(), ProfileComponent::uuid,
            Property.STREAM_CODEC.list(), ProfileComponent::properties,
            ::ProfileComponent
        )
    }

    data class Property(
        val name: String,
        val value: String,
        val signature: String?
    ) {
        companion object {
            val CODEC = StructCodec.of(
                "name", Codec.STRING, Property::name,
                "value", Codec.STRING, Property::value,
                "signature", Codec.STRING.optional(), Property::signature,
                ::Property
            )
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Property::name,
                StreamCodec.STRING, Property::value,
                StreamCodec.STRING.optional(), Property::signature,
                ::Property
            )
        }
    }
}