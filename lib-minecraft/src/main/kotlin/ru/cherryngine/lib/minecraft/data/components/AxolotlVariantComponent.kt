package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.entity.AxolotlMeta
import ru.cherryngine.lib.minecraft.tide.stream.EnumStreamCodec
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

data class AxolotlVariantComponent(
    val variant: AxolotlMeta.Variant,
) : DataComponent() {
    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofEnum(variant))
    }

    companion object {
        val STREAM_CODEC = StreamCodec.of(
            EnumStreamCodec<AxolotlMeta.Variant>(), AxolotlVariantComponent::variant,
            ::AxolotlVariantComponent
        )
    }
}