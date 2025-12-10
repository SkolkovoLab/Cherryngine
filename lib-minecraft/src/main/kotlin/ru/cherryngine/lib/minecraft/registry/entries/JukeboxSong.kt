package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonElementSerializer
import ru.cherryngine.lib.minecraft.utils.toNBT

@Serializable
data class JukeboxSong(
    val identifier: String,
    @SerialName("comparator_output")
    val comparatorOutput: Int,
    @Serializable(ComponentToJsonElementSerializer::class)
    val description: Component,
    @SerialName("length_in_seconds")
    val lengthInSeconds: Float,
    @SerialName("sound_event")
    val soundEvent: String,
) : RegistryEntry {
    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withInt("comparator_output", comparatorOutput)
            withCompound("description", description.toNBT())
            withFloat("length_in_seconds", lengthInSeconds)
            withString("sound_event", soundEvent)
        }
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

}