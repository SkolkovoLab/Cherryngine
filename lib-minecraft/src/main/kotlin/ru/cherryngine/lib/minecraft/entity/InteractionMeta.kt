package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class InteractionMeta : EntityMeta() {
    companion object : InteractionMeta()

    val WIDTH = index(MetadataEntry.Type.FLOAT, 1f)
    val HEIGHT = index(MetadataEntry.Type.FLOAT, 1f)
    val RESPONSIVE = index(MetadataEntry.Type.BOOLEAN, false)
}