package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.RegistryCodec
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.registry.registries.TrimMaterial
import ru.cherryngine.lib.minecraft.registry.registries.TrimMaterialRegistry
import ru.cherryngine.lib.minecraft.registry.registries.TrimPattern
import ru.cherryngine.lib.minecraft.registry.registries.TrimPatternRegistry
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class ArmorTrimComponent(
    val material: TrimMaterial,
    val pattern: TrimPattern
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CODEC.encode(CRC32CTranscoder, this))
    }

    companion object {
        val CODEC = StructCodec.of(
            "material", RegistryCodec.codec(TrimMaterialRegistry), ArmorTrimComponent::material,
            "pattern", RegistryCodec.codec(TrimPatternRegistry), ArmorTrimComponent::pattern,
            ::ArmorTrimComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            RegistryStreamCodec(TrimMaterialRegistry), ArmorTrimComponent::material,
            RegistryStreamCodec(TrimPatternRegistry), ArmorTrimComponent::pattern,
            ::ArmorTrimComponent
        )
    }
}