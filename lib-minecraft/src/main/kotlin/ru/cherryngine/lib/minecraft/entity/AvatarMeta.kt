package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.ClientSettings

@Suppress("PropertyName")
sealed class AvatarMeta : LivingEntityMeta() {
    companion object : AvatarMeta()

    val MAIN_HAND = index(MetadataEntry.Type.MAIN_HAND, ClientSettings.MainHand.RIGHT)
    val DISPLAYED_SKIN_PARTS = index(
        MetadataEntry.Type.BYTE,
        ClientSettings.DisplayedSkinParts.NONE,
        ClientSettings.DisplayedSkinParts::fromByte,
        ClientSettings.DisplayedSkinParts::toByte
    )
}