package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class PlayerMeta : AvatarMeta() {
    companion object : PlayerMeta()

    val ADDITIONAL_HEARTS = index(MetadataEntry.Type.FLOAT, 0f)
    val SCORE = index(MetadataEntry.Type.VAR_INT, 0)
    val LEFT_SHOULDER_ENTITY_DATA = index(MetadataEntry.Type.OPT_VAR_INT, null)
    val RIGHT_SHOULDER_ENTITY_DATA = index(MetadataEntry.Type.OPT_VAR_INT, null)
}