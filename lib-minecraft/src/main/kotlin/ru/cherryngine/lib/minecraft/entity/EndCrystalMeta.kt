package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class EndCrystalMeta : EntityMeta() {
    companion object : EndCrystalMeta()

    val BEAM_TARGET = index(MetadataEntry.Type.OPT_BLOCK_POSITION, null)
    val SHOW_BOTTOM = index(MetadataEntry.Type.BOOLEAN, true)
}