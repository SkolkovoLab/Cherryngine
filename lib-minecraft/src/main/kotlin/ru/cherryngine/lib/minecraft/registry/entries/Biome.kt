package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

@Serializable
data class Biome(
    val identifier: String,
    val temperature: Float = 1f,
    val downfall: Float = 1f,
    @SerialName("has_precipitation")
    val hasPrecipitation: Boolean = false,
    val temperatureModifier: String? = null,
    val fogColor: Int? = null,
    val foliageColor: Int? = null,
    val grassColor: Int? = null,
    val grassColorModifier: String? = null,
    val moodSound: MoodSound? = null,
    val music: List<WeightedBackgroundMusic>? = null,
    val musicVolume: Float? = null,
    val ambientAdditions: AmbientAdditions? = null,
    val ambientLoop: String? = null,
    val particle: BiomeParticles? = null,
    val skyColor: Int,
    val waterColor: Int,
    val waterFogColor: Int,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        val effects = nbt {
            if (fogColor != null) withInt("fog_color", fogColor)
            if (foliageColor != null) withInt("foliage_color", foliageColor)
            if (grassColor != null) withInt("grass_color", grassColor)
            if (grassColorModifier != null) withString("grass_color_modifier", grassColorModifier)
            if (moodSound != null) withCompound("mood_sound", moodSound.toNBT())
            if (music != null) withList("music", BinaryTagTypes.COMPOUND, music.map { music -> music.getNbt() })
            if (musicVolume != null) withFloat("music_volume", musicVolume)
            if (ambientAdditions != null) withCompound("additions_sound", ambientAdditions.toNBT())
            if (particle != null) withCompound("particle", particle.toNBT())
            if (ambientLoop != null) withString("ambient_sound", ambientLoop)
            withInt("sky_color", skyColor)
            withInt("water_color", waterColor)
            withInt("water_fog_color", waterFogColor)
        }
        return nbt {
            withFloat("downfall", downfall)
            withCompound("effects", effects)
            withBoolean("has_precipitation", hasPrecipitation)
            withFloat("temperature", temperature)
            if (temperatureModifier != null) withString("temperature_modifier", temperatureModifier)
        }
    }

    @Serializable
    data class WeightedBackgroundMusic(val music: BackgroundMusic, val weight: Int) : NbtWritable {

        override fun getNbt(): CompoundBinaryTag {
            return nbt {
                withCompound("data", music.toNBT())
                withInt("weight", weight)
            }
        }

    }

    @Serializable
    data class MoodSound(
        val blockSearchExtent: Int,
        val soundPositionOffset: Double,
        @Serializable(with = KeySerializer::class)
        val sound: Key,
        val tickDelay: Int,
    ) {
        fun toNBT(): CompoundBinaryTag {
            return nbt {
                withInt("block_search_extent", blockSearchExtent)
                withDouble("offset", soundPositionOffset)
                withString("sound", sound.asString())
                withInt("tick_delay", tickDelay)
            }
        }
    }

    @Serializable
    data class BackgroundMusic(
        val maxDelay: Int,
        val minDelay: Int,
        val replaceCurrentMusic: Boolean,
        @Serializable(with = KeySerializer::class)
        val sound: Key,
    ) {
        fun toNBT(): CompoundBinaryTag {
            return nbt {
                withCompound(
                    "sound", SoundEvent.CustomSoundEvent.CODEC.encode(
                        BinaryTagTranscoder,
                        SoundEvent.CustomSoundEvent(sound, null)
                    ) as CompoundBinaryTag
                )
                withInt("max_delay", maxDelay)
                withInt("min_delay", minDelay)
                withBoolean("replace_current_music", replaceCurrentMusic)
            }
        }
    }

    @Serializable
    data class AmbientAdditions(
        @Serializable(with = KeySerializer::class)
        val sound: Key,
        val tickChance: Double,
    ) {
        fun toNBT(): CompoundBinaryTag {
            return nbt {
                withString("sound", sound.asString())
                withDouble("tick_chance", tickChance)
            }
        }
    }

    @Serializable
    data class BiomeParticles(
        val options: ParticleOptions,
        val probability: Float,
    ) {
        fun toNBT(): CompoundBinaryTag {
            return nbt {
                withCompound("options") {
                    withString("type", options.type)
                }
                withFloat("probability", probability)
            }
        }
    }

    @Serializable
    data class ParticleOptions(
        val type: String,
    )
}