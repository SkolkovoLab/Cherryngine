package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object WolfVariantRegistry : DataDrivenRegistry<WolfVariant>() {

    override val identifier: String = "minecraft:wolf_variant"

}

@Serializable
data class WolfVariant(
    val identifier: String,
    val angry: String,
    val tame: String,
    val wild: String,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return WolfVariantRegistry.getProtocolIdByEntry(this)
    }

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