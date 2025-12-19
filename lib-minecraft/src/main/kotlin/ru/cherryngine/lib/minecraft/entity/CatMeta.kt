package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.network.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.registry.Registries

@Suppress("PropertyName")
sealed class CatMeta : TameableAnimalMeta() {
    companion object : CatMeta()

    val VARIANT = index(MetadataEntry.Type.CAT_VARIANT, Registries.catVariant.getValue("black"))
    val IS_LYING = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_RELAXED = index(MetadataEntry.Type.BOOLEAN, false)
    val COLLAR_COLOR = index<Int, DyeColor>(
        MetadataEntry.Type.VAR_INT,
        DyeColor.RED,
        ::fromIndex,
        ::intIndex
    )
}