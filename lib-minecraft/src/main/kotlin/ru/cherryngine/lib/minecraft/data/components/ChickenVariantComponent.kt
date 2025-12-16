package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.ChickenVariant

class ChickenVariantComponent(
    val variant: ChickenVariant,
) : DynamicVariantComponent<ChickenVariant>(variant, Registries.chickenVariant) {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofRegistryEntry(Registries.chickenVariant,variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            Registries.chickenVariant.streamCodec, ChickenVariantComponent::variant,
            ::ChickenVariantComponent
        )
    }
}