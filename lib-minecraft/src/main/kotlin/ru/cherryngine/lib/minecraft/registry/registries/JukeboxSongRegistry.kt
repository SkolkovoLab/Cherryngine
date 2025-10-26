package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.text.Component
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.ComponentToJsonSerializer
import ru.cherryngine.lib.minecraft.utils.toNBT

object JukeboxSongRegistry : DataDrivenRegistry<JukeboxSong>() {
    override val identifier: String = "minecraft:jukebox_song"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class JukeboxSong(
    val identifier: String,
    val comparatorOutput: Int,
    @Serializable(ComponentToJsonSerializer::class)
    val description: Component,
    val lengthInSeconds: Float,
    val sound: String,
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return JukeboxSongRegistry.getProtocolIdByEntry(this)
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withInt("comparator_output", comparatorOutput)
            withCompound("description", description.toNBT())
            withFloat("length_in_seconds", lengthInSeconds)
            withString("sound_event", sound)
        }
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

}