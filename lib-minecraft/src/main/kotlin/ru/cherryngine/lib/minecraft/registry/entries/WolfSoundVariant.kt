package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class WolfSoundVariant(
    val identifier: String,
    @SerialName("ambient_sound")
    val ambientSound: String,
    @SerialName("death_sound")
    val deathSound: String,
    @SerialName("growl_sound")
    val growlSound: String,
    @SerialName("hurt_sound")
    val hurtSound: String,
    @SerialName("pant_sound")
    val pantSound: String,
    @SerialName("whine_sound")
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