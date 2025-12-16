package ru.cherryngine.lib.minecraft.registry.types

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.data.DataComponentPatch
import ru.cherryngine.lib.minecraft.item.ItemStack
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer
import ru.cherryngine.lib.minecraft.utils.registry.StaticProtocolObject

@Serializable
data class Item(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
    val translationKey: String,
    val components: JsonObject,
    val correspondingBlock: String? = null,
    val spawnEggProperties: SpawnEggProperties? = null,
) : StaticProtocolObject {
    fun toItemStack(amount: Int = 1): ItemStack {
        return ItemStack(this, amount, DataComponentPatch.EMPTY)
    }

    @Serializable
    data class SpawnEggProperties(
        val entityType: String,
    )
}