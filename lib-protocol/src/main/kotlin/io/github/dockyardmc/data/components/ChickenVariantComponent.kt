package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.registry.registries.ChickenVariant
import io.github.dockyardmc.registry.registries.ChickenVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

class ChickenVariantComponent(
    val variant: ChickenVariant
) : DynamicVariantComponent<ChickenVariant>(variant, ChickenVariantRegistry) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(ChickenVariantRegistry), ChickenVariantComponent::variant,
            ::ChickenVariantComponent
        )
    }
}