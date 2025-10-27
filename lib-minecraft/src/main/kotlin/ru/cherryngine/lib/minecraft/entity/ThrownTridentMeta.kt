package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ThrownTridentMeta : AbstractArrowMeta() {
    companion object : ThrownTridentMeta()

    val LOYALTY_LEVEL = index(MetadataEntry.Type.BYTE, 0)
    val HAS_ENCHANTMENT_GLINT = index(MetadataEntry.Type.BOOLEAN, false)
}