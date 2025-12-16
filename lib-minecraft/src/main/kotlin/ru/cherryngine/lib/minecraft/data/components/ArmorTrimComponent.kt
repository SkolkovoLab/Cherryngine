package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.CRC32CTranscoder
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.network.stream_codec.StreamCodec
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.types.TrimMaterial
import ru.cherryngine.lib.minecraft.registry.types.TrimPattern

data class ArmorTrimComponent(
    val material: TrimMaterial,
    val pattern: TrimPattern,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CODEC.encode(CRC32CTranscoder, this))
    }

    companion object {
        val CODEC = StructCodec.of(
            "material", Registries.trimMaterial.keyCodec, ArmorTrimComponent::material,
            "pattern", Registries.trimPattern.keyCodec, ArmorTrimComponent::pattern,
            ::ArmorTrimComponent
        )

        val STREAM_CODEC = StreamCodec.of(
            Registries.trimMaterial.streamCodec, ArmorTrimComponent::material,
            Registries.trimPattern.streamCodec, ArmorTrimComponent::pattern,
            ::ArmorTrimComponent
        )
    }
}