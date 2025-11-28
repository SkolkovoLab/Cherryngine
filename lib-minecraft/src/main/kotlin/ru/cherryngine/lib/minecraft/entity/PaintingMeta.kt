package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.keys.PaintingVariants

sealed class PaintingMeta : HangingMeta() {
    companion object : PaintingMeta()

    val VARIANT = index(MetadataEntry.Type.PAINTING_VARIANT, PaintingVariants.KEBAB)
}