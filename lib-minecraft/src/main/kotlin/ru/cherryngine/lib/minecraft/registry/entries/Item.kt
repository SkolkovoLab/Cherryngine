package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import ru.cherryngine.lib.minecraft.data.DataComponentPatch
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class Item(
    val identifier: String,
    val translationKey: String,
    val components: JsonObject,
    val correspondingBlock: String? = null,
    val spawnEggProperties: SpawnEggProperties? = null,
) : RegistryEntry {
    fun toItemStack(amount: Int = 1): ItemStack {
        return ItemStack(this, amount, DataComponentPatch.EMPTY)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

    @Serializable
    data class SpawnEggProperties(
        val entityType: String,
    )
}