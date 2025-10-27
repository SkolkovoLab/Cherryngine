package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.DyeColor
import ru.cherryngine.lib.minecraft.registry.WolfSoundVariants
import ru.cherryngine.lib.minecraft.registry.WolfVariants

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
    val ANGER_TIME = index(MetadataEntry.Type.VAR_INT, 0)
    val VARIANT = index(MetadataEntry.Type.WOLF_VARIANT, WolfVariants.PALE)
    val SOUND_VARIANT = index(MetadataEntry.Type.WOLF_SOUND_VARIANT, WolfSoundVariants.CLASSIC)
}