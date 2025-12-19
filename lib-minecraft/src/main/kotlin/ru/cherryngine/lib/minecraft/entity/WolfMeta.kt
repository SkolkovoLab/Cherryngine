package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.network.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.registry.Registries

@Suppress("PropertyName")
sealed class WolfMeta : TameableAnimalMeta() {
    companion object : WolfMeta()

    val IS_BEGGING = index(MetadataEntry.Type.BOOLEAN, false)
    val COLLAR_COLOR = index<Int, DyeColor>(
        MetadataEntry.Type.VAR_INT,
        DyeColor.RED,
        ::fromIndex,
        ::intIndex
    )
    val ANGER_TIME = index(MetadataEntry.Type.VAR_LONG, -1)
    val VARIANT = index(MetadataEntry.Type.WOLF_VARIANT, Registries.wolfVariant.getValue("pale"))
    val SOUND_VARIANT = index(MetadataEntry.Type.WOLF_SOUND_VARIANT, Registries.wolfSoundVariant.getValue("classic"))
}