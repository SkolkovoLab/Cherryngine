package ru.cherryngine.lib.minecraft.r2

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.MapCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class TrimPattern(
    val assetId: Key,
    val description: Component,
    val decal: Boolean,
) {
    companion object {
        private val MAP_CODEC = MapCodec(Codec.STRING, Codec.STRING).default(mapOf())
        val CODEC = StructCodec.of(
            "asset_id", Codec.KEY, TrimPattern::assetId,
            "description", ComponentCodec, TrimPattern::description,
            "decal", Codec.BOOLEAN, TrimPattern::decal,
            ::TrimPattern
        )
    }
}