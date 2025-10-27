package ru.cherryngine.lib.minecraft.entity

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.protocol.types.ClientSettings

@Suppress("PropertyName")
sealed class PlayerMeta : LivingEntityMeta() {
    companion object : PlayerMeta()

    val ADDITIONAL_HEARTS = index(MetadataEntry.Type.FLOAT, 0f)
    val SCORE = index(MetadataEntry.Type.VAR_INT, 0)
    val DISPLAYED_SKIN_PARTS = index(
        MetadataEntry.Type.BYTE,
        ClientSettings.DisplayedSkinParts.NONE,
        ClientSettings.DisplayedSkinParts::fromByte,
        ClientSettings.DisplayedSkinParts::toByte
    )
    val MAIN_HAND = index<Byte, ClientSettings.MainHand>(
        MetadataEntry.Type.BYTE,
        ClientSettings.MainHand.RIGHT,
        ::fromIndex,
        ::byteIndex
    )
    val LEFT_SHOULDER_ENTITY_DATA = index(MetadataEntry.Type.NBT, CompoundBinaryTag.empty())
    val RIGHT_SHOULDER_ENTITY_DATA = index(MetadataEntry.Type.NBT, CompoundBinaryTag.empty())
}