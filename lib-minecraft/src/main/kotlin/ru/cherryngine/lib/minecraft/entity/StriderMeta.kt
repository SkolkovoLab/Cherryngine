package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class StriderMeta : AgeableMobMeta() {
    companion object : StriderMeta()

    val FUNGUS_BOOST = index(MetadataEntry.Type.VAR_INT, 0)
    val IS_SHAKING = index(MetadataEntry.Type.BOOLEAN, false)
}