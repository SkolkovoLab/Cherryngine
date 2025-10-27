package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class ArrowMeta : AbstractArrowMeta() {
    companion object : ArrowMeta()

    val COLOR = index(MetadataEntry.Type.VAR_INT, -1)
}