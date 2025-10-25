package io.github.dockyardmc.data.components

import io.github.dockyardmc.codec.RegistryStreamCodec
import io.github.dockyardmc.data.CRC32CHasher
import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.data.HashHolder
import io.github.dockyardmc.data.StaticHash
import io.github.dockyardmc.registry.registries.CatVariant
import io.github.dockyardmc.registry.registries.CatVariantRegistry
import io.github.dockyardmc.tide.stream.StreamCodec

data class CatVariantComponent(
    val variant: CatVariant
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(CatVariantRegistry), CatVariantComponent::variant,
            ::CatVariantComponent
        )
    }
}