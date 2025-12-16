package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.key.Key
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.ComponentCodec
import ru.cherryngine.lib.minecraft.codec.StructCodec

data class PaintingVariant(
    val width: Int,
    val height: Int,
    val assetId: Key,
    val title: Component?,
    val author: Component?,
) {
    companion object {
        val CODEC = StructCodec.of(
            "width", Codec.INT, PaintingVariant::width,
            "height", Codec.INT, PaintingVariant::height,
            "asset_id", Codec.KEY, PaintingVariant::assetId,
            "title", ComponentCodec.optional(), PaintingVariant::title,
            "author", ComponentCodec.optional(), PaintingVariant::author,
            ::PaintingVariant
        )
    }
}