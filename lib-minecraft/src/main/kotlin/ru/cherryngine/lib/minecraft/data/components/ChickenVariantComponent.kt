package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.registry.registries.ChickenVariant
import ru.cherryngine.lib.minecraft.registry.registries.ChickenVariantRegistry
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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