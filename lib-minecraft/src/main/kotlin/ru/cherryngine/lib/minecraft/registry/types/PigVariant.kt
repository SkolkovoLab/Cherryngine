package ru.cherryngine.lib.minecraft.registry.types

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class PigVariant(
    val assetId: String,
) {
    companion object {
        val CODEC = StructCodec.of(
            "asset_id", Codec.STRING, PigVariant::assetId,
            ::PigVariant
        )
    }
}