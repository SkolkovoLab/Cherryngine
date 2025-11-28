package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class WolfVariant(
    val identifier: String,
    val angry: String,
    val tame: String,
    val wild: String,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withCompound("assets") {
                withString("angry", angry)
                withString("tame", tame)
                withString("wild", wild)
            }
        }
    }
}