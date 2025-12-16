package ru.cherryngine.lib.minecraft.registry.types

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class CowVariant(
    val assetId: String,
) {
    companion object {
        val CODEC = StructCodec.of(
            "asset_id", Codec.STRING, CowVariant::assetId,
            ::CowVariant
        )
    }
}