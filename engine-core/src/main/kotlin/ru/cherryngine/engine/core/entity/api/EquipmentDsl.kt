@file:Suppress("unused")

package ru.cherryngine.engine.core.entity.api

import net.minestom.server.item.ItemStack
import ru.cherryngine.engine.core.entity.SEquipment

fun equipment(init: SEquipment.() -> Unit): SEquipment {
    val equipment = SEquipment()
    equipment.init()
    return equipment
}

var SEquipment.mainHand: ItemStack
    get() = this.mainHandItem
    set(value) {
        this.mainHandItem = value
    }

var SEquipment.offHand: ItemStack
    get() = this.offHandItem
    set(value) {
        this.offHandItem = value
    }

var SEquipment.helmetItem: ItemStack
    get() = this.helmet
    set(value) {
        this.helmet = value
    }

var SEquipment.chestplateItem: ItemStack
    get() = this.chestplate
    set(value) {
        this.chestplate = value
    }

var SEquipment.leggingsItem: ItemStack
    get() = this.leggings
    set(value) {
        this.leggings = value
    }

var SEquipment.bootsItem: ItemStack
    get() = this.boots
    set(value) {
        this.boots = value
    }

var SEquipment.bodyEquipmentItem: ItemStack
    get() = this.bodyEquipment
    set(value) {
        this.bodyEquipment = value
    }