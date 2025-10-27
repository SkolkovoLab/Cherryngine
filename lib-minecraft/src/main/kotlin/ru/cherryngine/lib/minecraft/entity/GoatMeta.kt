package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class GoatMeta : AgeableMobMeta() {
    companion object : GoatMeta()

    val IS_SCREAMING_GOAT = index(MetadataEntry.Type.BOOLEAN, false)
    val HAS_LEFT_HORN = index(MetadataEntry.Type.BOOLEAN, true)
    val HAS_RIGHT_HORN = index(MetadataEntry.Type.BOOLEAN, true)
}