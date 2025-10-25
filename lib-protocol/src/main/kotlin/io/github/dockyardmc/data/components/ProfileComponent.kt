package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.protocol.DataComponentHashable
import io.github.dockyardmc.tide.stream.StreamCodec
import io.github.dockyardmc.utils.toIntArray
import java.util.*

data class ProfileComponent(
    val name: String?,
    val uuid: UUID?,
    val properties: List<Property>
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return CRC32CHasher.of {
            optional("name", name, CRC32CHasher::ofString)
            optional("uuid", uuid?.toIntArray(), CRC32CHasher::ofIntArray)
            defaultStructList("properties", properties, listOf(), Property::hashStruct)
        }
    }

    companion object {
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
    ) : DataComponentHashable {
        override fun hashStruct(): HashHolder {
            return CRC32CHasher.of {
                static("name", CRC32CHasher.ofString(name))
                static("value", CRC32CHasher.ofString(value))
                optional("signature", signature, CRC32CHasher::ofString)
            }
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Property::name,
                StreamCodec.STRING, Property::value,
                StreamCodec.STRING.optional(), Property::signature,
                ::Property
            )
        }
    }
}