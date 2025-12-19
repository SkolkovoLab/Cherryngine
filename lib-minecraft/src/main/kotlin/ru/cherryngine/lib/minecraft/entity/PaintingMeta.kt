package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.registry.Registries

sealed class PaintingMeta : HangingMeta() {
    companion object : PaintingMeta()

    val VARIANT = index(MetadataEntry.Type.PAINTING_VARIANT, Registries.paintingVariant.getValue("kebab"))
}