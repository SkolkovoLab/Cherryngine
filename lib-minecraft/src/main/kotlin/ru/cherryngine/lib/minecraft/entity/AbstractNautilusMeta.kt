package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AbstractNautilusMeta : TameableAnimalMeta() {
    companion object : AbstractNautilusMeta()

    val DASH = index(MetadataEntry.Type.BOOLEAN, false)
}