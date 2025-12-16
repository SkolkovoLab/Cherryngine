package ru.cherryngine.lib.minecraft.registry.types

import net.kyori.adventure.util.RGBLike
import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.envattr.EnvironmentAttributeMap
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

data class Biome(
    val climateSettings: ClimateSettings,
    val attributes: EnvironmentAttributeMap,
    val specialEffects: BiomeSpecialEffects,
) {
    companion object {
        val CODEC = StructCodec.of(
            StructCodec.INLINE, ClimateSettings.CODEC, Biome::climateSettings,
            "attributes", EnvironmentAttributeMap.CODEC.default(EnvironmentAttributeMap.EMPTY), Biome::attributes,
            "effects", BiomeSpecialEffects.CODEC, Biome::specialEffects,
            ::Biome
        )
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