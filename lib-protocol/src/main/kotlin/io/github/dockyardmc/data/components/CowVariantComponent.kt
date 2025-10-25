package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.registry.registries.CowVariant
import io.github.dockyardmc.registry.registries.CowVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

class CowVariantComponent(
    val variant: CowVariant
) : DynamicVariantComponent<CowVariant>(variant, CowVariantRegistry) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(CowVariantRegistry), CowVariantComponent::variant,
            ::CowVariantComponent
        )
    }
}