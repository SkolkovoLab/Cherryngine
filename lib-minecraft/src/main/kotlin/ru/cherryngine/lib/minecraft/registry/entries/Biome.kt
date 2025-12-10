package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.BinaryTagTypes
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.extentions.asRGB
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.CustomColor
import ru.cherryngine.lib.minecraft.utils.kotlinx.CustomColorHexSerializer
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

@Serializable
data class Biome(
    val identifier: String,
    val attributes: Map<String, JsonElement> = mapOf(), // TODO
    val carvers: JsonElement,
    @SerialName("creature_spawn_probability")
    val creatureSpawnProbability: Float? = null,
    val downfall: Float = 1f,
    val effects: Effects,
    val features: List<List<String>>,
    @SerialName("has_precipitation")
    val hasPrecipitation: Boolean,
    @SerialName("spawn_costs")
    val spawnCosts: JsonObject,
    val spawners: JsonObject,
    val temperature: Float = 1f,
    @SerialName("temperature_modifier")
    val temperatureModifier: String? = null,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return nbt {
            withFloat("downfall", downfall)
            withCompound("effects", effects.toNBT())
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

    @Serializable
    data class Effects(
        @SerialName("fog_color")
        @Serializable(CustomColorHexSerializer::class)
        val fogColor: CustomColor? = null,
        @SerialName("dry_foliage_color")
        @Serializable(CustomColorHexSerializer::class)
        val dryFoliageColor: CustomColor? = null,
        @SerialName("foliage_color")
        @Serializable(CustomColorHexSerializer::class)
        val foliageColor: CustomColor? = null,
        @SerialName("grass_color")
        @Serializable(CustomColorHexSerializer::class)
        val grassColor: CustomColor? = null,
        @SerialName("grass_color_modifier")
        val grassColorModifier: String? = null,
        @SerialName("mood_sound")
        val moodSound: MoodSound? = null,
        @SerialName("music")
        val music: List<WeightedBackgroundMusic>? = null,
        @SerialName("music_volume")
        val musicVolume: Float? = null,
        @SerialName("additions_sound")
        val ambientAdditions: AmbientAdditions? = null,
        @SerialName("ambient_sound")
        val ambientLoop: String? = null,
        val particle: BiomeParticles? = null,
        @SerialName("water_color")
        @Serializable(CustomColorHexSerializer::class)
        val waterColor: CustomColor,
        @SerialName("water_fog_color")
        @Serializable(CustomColorHexSerializer::class)
        val waterFogColor: CustomColor? = null,
    ) {
        fun toNBT(): CompoundBinaryTag {
            return nbt {
                if (fogColor != null) withInt("fog_color", fogColor.asRGB())
                if (foliageColor != null) withInt("foliage_color", foliageColor.asRGB())
                if (grassColor != null) withInt("grass_color", grassColor.asRGB())
                if (grassColorModifier != null) withString("grass_color_modifier", grassColorModifier)
                if (moodSound != null) withCompound("mood_sound", moodSound.toNBT())
                if (music != null) withList("music", BinaryTagTypes.COMPOUND, music.map { music -> music.getNbt() })
                if (musicVolume != null) withFloat("music_volume", musicVolume)
                if (ambientAdditions != null) withCompound("additions_sound", ambientAdditions.toNBT())
                if (particle != null) withCompound("particle", particle.toNBT())
                if (ambientLoop != null) withString("ambient_sound", ambientLoop)
                withInt("water_color", waterColor.asRGB())
            }
        }
    }
}