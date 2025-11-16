package ru.cherryngine.integration.grim.player

import ac.grim.grimac.platform.api.player.PlatformInventory
import com.github.retrooper.packetevents.protocol.item.ItemStack

class PlatformInventoryImpl : PlatformInventory {
    override fun getItemInHand(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getItemInOffHand(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getStack(
        bukkitSlot: Int,
        vanillaSlot: Int,
    ): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getHelmet(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getChestplate(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getLeggings(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getBoots(): ItemStack? {
        return ItemStack.EMPTY
    }

    override fun getContents(): Array<out ItemStack?>? {
        return emptyArray()
    }

    override fun getOpenInventoryKey(): String? {
        return ""
    }
}