package ru.cherryngine.lib.minecraft.registry.types

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class BannerPattern(
    val assetId: String,
    val translationKey: String,
) {
    companion object {
        val CODEC = StructCodec.of(
            "asset_id", Codec.STRING, BannerPattern::assetId,
            "translation_key", Codec.STRING, BannerPattern::translationKey,
            ::BannerPattern
        )
    }
}