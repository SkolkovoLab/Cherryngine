package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class CreakingMeta : MobMeta() {
    companion object : CreakingMeta()

    val CAN_MOVE = index(MetadataEntry.Type.BOOLEAN, true)
    val IS_ACTIVE = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_TEARING_DOWN = index(MetadataEntry.Type.BOOLEAN, false)
    val HOME_POS = index(MetadataEntry.Type.OPT_BLOCK_POSITION, null)
}