package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.codec.transcoder.BinaryTagTranscoder
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.protocol.NbtWritable
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.registry.registries.EnvironmentAttributeMap
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

data class Biome(
    val climateSettings: ClimateSettings,
    val attributes: EnvironmentAttributeMap,
    val specialEffects: BiomeSpecialEffects,
) : RegistryEntry {
    lateinit var identifier: String
        private set

    companion object {
        val CODEC = StructCodec.of(
            StructCodec.INLINE, ClimateSettings.CODEC, Biome::climateSettings,
            "attributes", EnvironmentAttributeMap.CODEC.default(EnvironmentAttributeMap.EMPTY), Biome::attributes,
            "effects", BiomeSpecialEffects.CODEC, Biome::specialEffects,
            ::Biome
        )

    }

    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        return CODEC.encode(BinaryTagTranscoder, this) as CompoundBinaryTag
    }

    override fun setIdentifier(identifier: String) {
        this.identifier = identifier
    }

    data class ClimateSettings(
        val hasPrecipitation: Boolean,
        val temperature: Float,
        val temperatureModifier: TemperatureModifier?,
        val downfall: Float,
    ) {
        companion object {
            val CODEC: StructCodec<ClimateSettings> = StructCodec.of(
                "has_precipitation", Codec.BOOLEAN, ClimateSettings::hasPrecipitation,
                "temperature", Codec.FLOAT, ClimateSettings::temperature,
                "temperature_modifier", TemperatureModifier.CODEC.optional(), ClimateSettings::temperatureModifier,
                "downfall", Codec.FLOAT, ClimateSettings::downfall,
                ::ClimateSettings
            )
        }
    }

    enum class TemperatureModifier {
        NONE, FROZEN;

        companion object {
            val CODEC = Codec.enum<TemperatureModifier>()
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

    data class BiomeSpecialEffects(
        val waterColor: RGBLike,
        val foliageColorOverride: RGBLike?,
        val dryFoliageColorOverride: RGBLike?,
        val grassColorOverride: RGBLike?,
        val grassColorModifier: GrassColorModifier?,
    ) {
        companion object {
            val CODEC: StructCodec<BiomeSpecialEffects> = StructCodec.of(
                "water_color", RGBLikeImpl.STRING_CODEC, BiomeSpecialEffects::waterColor,
                "foliage_color", RGBLikeImpl.STRING_CODEC.optional(), BiomeSpecialEffects::foliageColorOverride,
                "dry_foliage_color", RGBLikeImpl.STRING_CODEC.optional(), BiomeSpecialEffects::dryFoliageColorOverride,
                "grass_color", RGBLikeImpl.STRING_CODEC.optional(), BiomeSpecialEffects::grassColorOverride,
                "grass_color_modifier", GrassColorModifier.CODEC.optional(), BiomeSpecialEffects::grassColorModifier,
                ::BiomeSpecialEffects
            )
        }


        enum class GrassColorModifier {
            NONE, DARK_FOREST, SWAMP;

            companion object {
                val CODEC = Codec.enum<GrassColorModifier>()
            }
        }
    }
}