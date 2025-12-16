package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.CowVariant

class CowVariantComponent(
    val variant: CowVariant,
) : DynamicVariantComponent<CowVariant>(variant, Registries.cowVariant) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(Registries.cowVariant, variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.cowVariant.streamCodec, CowVariantComponent::variant,
            ::CowVariantComponent
        )
    }
}