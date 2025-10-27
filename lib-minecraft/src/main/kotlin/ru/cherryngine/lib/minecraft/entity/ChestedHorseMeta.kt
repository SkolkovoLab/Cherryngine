package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ChestedHorseMeta : AbstractHorse() {
    companion object : ChestedHorseMeta()

    val HAS_CHEST = index(MetadataEntry.Type.BOOLEAN, false)
}