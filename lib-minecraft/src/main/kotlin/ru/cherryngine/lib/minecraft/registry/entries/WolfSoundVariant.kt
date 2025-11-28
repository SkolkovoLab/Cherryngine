package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class WolfSoundVariant(
    val identifier: String,
    val ambientSound: String,
    val deathSound: String,
    val growlSound: String,
    val hurtSound: String,
    val pantSound: String,
    val whineSound: String,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withString("ambient_sound", ambientSound)
            withString("death_sound", deathSound)
            withString("growl_sound", growlSound)
            withString("hurt_sound", hurtSound)
            withString("pant_sound", pantSound)
            withString("whine_sound", whineSound)
        }
    }
}