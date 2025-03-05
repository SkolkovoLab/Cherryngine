package ru.cherryngine.engine.core.entity

import net.minestom.server.entity.EquipmentSlot
import net.minestom.server.item.ItemStack
import net.minestom.server.network.packet.server.play.EntityEquipmentPacket

data class SEquipment(
    var mainHandItem: ItemStack = ItemStack.AIR,
    var offHandItem: ItemStack = ItemStack.AIR,
    var helmet: ItemStack = ItemStack.AIR,
    var chestplate: ItemStack = ItemStack.AIR,
    var leggings: ItemStack = ItemStack.AIR,
    var boots: ItemStack = ItemStack.AIR,
    var bodyEquipment: ItemStack = ItemStack.AIR,
) {
    fun packet(entityId: Int): EntityEquipmentPacket {
        val map = mapOf(
            EquipmentSlot.MAIN_HAND to mainHandItem,
            EquipmentSlot.OFF_HAND to offHandItem,
            EquipmentSlot.BOOTS to boots,
            EquipmentSlot.LEGGINGS to leggings,
            EquipmentSlot.CHESTPLATE to chestplate,
            EquipmentSlot.HELMET to helmet,
            EquipmentSlot.BODY to bodyEquipment,
        )
        return EntityEquipmentPacket(entityId, map)
    }

    fun packet(entityId: Int, slot: EquipmentSlot): EntityEquipmentPacket {
        val item = when (slot) {
            EquipmentSlot.MAIN_HAND -> mainHandItem
            EquipmentSlot.OFF_HAND -> offHandItem
            EquipmentSlot.BOOTS -> boots
            EquipmentSlot.LEGGINGS -> leggings
            EquipmentSlot.CHESTPLATE -> chestplate
            EquipmentSlot.HELMET -> helmet
            EquipmentSlot.BODY -> bodyEquipment
        }
        return EntityEquipmentPacket(entityId, mapOf(slot to item))
    }
}