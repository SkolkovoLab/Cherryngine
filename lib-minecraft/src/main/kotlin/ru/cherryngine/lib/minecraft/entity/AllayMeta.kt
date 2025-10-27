package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class AllayMeta : MobMeta() {
    companion object : AllayMeta()

    val IS_DANCING = index(MetadataEntry.Type.BOOLEAN, false)
    val CAN_DUPLICATE = index(MetadataEntry.Type.BOOLEAN, true)
}