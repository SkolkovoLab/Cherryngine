package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.data.DataComponentPatch
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object ItemRegistry : DataDrivenRegistry<Item>() {
    override val identifier: String = "minecraft:item"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class Item(
    val identifier: String,
    val displayName: String,
    val maxStack: Int,
    val consumeSound: String,
    val canFitInsideContainers: Boolean,
    val isEnchantable: Boolean,
    val isStackable: Boolean,
    val isDamageable: Boolean,
    val isBlock: Boolean,
    @Transient
    var defaultComponents: List<DataComponent>? = null
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return ItemRegistry.getProtocolIdByEntry(this)
    }

    fun toItemStack(amount: Int = 1): ItemStack {
        return ItemStack(this, amount, DataComponentPatch.EMPTY)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

}