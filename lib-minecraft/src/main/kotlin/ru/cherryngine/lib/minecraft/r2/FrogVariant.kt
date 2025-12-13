package ru.cherryngine.lib.minecraft.r2

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class FrogVariant(
    val assetId: String,
) {
    companion object {
        val CODEC = StructCodec.of(
            "asset_id", Codec.STRING, FrogVariant::assetId,
            ::FrogVariant
        )
    }
}