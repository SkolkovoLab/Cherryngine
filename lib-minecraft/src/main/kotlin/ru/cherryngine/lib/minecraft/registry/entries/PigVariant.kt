package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class PigVariant(
    val identifier: String,
    @SerialName("asset_id")
    val assetId: String,
    val model: String? = null,
    @SerialName("spawn_conditions")
    val spawnConditions: List<SpawnCondition>,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("asset_id", assetId)
        }
    }
}