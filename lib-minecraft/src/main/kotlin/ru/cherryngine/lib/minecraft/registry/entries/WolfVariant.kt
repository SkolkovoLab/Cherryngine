package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class WolfVariant(
    val identifier: String,
    val assets: Assets,
    @SerialName("spawn_conditions")
    val spawnConditions: List<SpawnCondition>,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withCompound("assets") {
                withString("angry", assets.angry)
                withString("tame", assets.tame)
                withString("wild", assets.wild)
            }
        }
    }

    @Serializable
    data class Assets(
        val angry: String,
        val tame: String,
        val wild: String,
    )
}