package ru.cherryngine.lib.minecraft.protocol.types

import ru.cherryngine.lib.minecraft.data.CRC32CHasher
import ru.cherryngine.lib.minecraft.data.HashHolder
import ru.cherryngine.lib.minecraft.data.StaticHash
import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable

enum class EquipmentSlot(val nbtName: String): DataComponentHashable {
    MAIN_HAND("mainhand"),
    OFF_HAND("offhand"),
    BOOTS("feet"),
    LEGGINGS("legs"),
    CHESTPLATE("chest"),
    HELMET("head"),
    BODY("body"),
    SADDLE("saddle");

    override fun hashStruct(): HashHolder {
        return StaticHash(CRC32CHasher.ofString(nbtName))
    }

    companion object {
        fun isBody(equipmentSlot: EquipmentSlot?): Boolean {
            if (equipmentSlot == null) return false
            if (equipmentSlot == MAIN_HAND) return false
            if (equipmentSlot == OFF_HAND) return false
            if (equipmentSlot == SADDLE) return false
            return true
        }
    }
}