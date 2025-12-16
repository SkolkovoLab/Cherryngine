package ru.cherryngine.lib.minecraft.registry.types

import ru.cherryngine.lib.minecraft.codec.Codec
import ru.cherryngine.lib.minecraft.codec.StructCodec
import ru.cherryngine.lib.minecraft.codec.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.envattr.EnvironmentAttributeMap
import ru.cherryngine.lib.minecraft.utils.toKey

data class DimensionType(
    val hasFixedTime: Boolean,
    val hasSkylight: Boolean,
    val hasCeiling: Boolean,
    val coordinateScale: Double,
    val minY: Int,
    val height: Int,
    val logicalHeight: Int,
    val infiniburn: String,
    val ambientLight: Float,
    val monsterSpawnLightLevel: MonsterSpawnLightLevel,
    val monsterSpawnBlockLightLimit: Int,
//    val skybox: Skybox,
//    val cardinalLight: CardinalLight,
    val attributes: EnvironmentAttributeMap,
//    val timelines: RegistryTag<Timeline>,
) {
    companion object {
        val CODEC = StructCodec.of(
            "has_fixed_time",
            Codec.BOOLEAN.default(false),
            DimensionType::hasFixedTime,
            "has_skylight",
            Codec.BOOLEAN,
            DimensionType::hasSkylight,
            "has_ceiling",
            Codec.BOOLEAN,
            DimensionType::hasCeiling,
            "coordinate_scale",
            Codec.DOUBLE,
            DimensionType::coordinateScale,
            "min_y",
            Codec.INT,
            DimensionType::minY,
            "height",
            Codec.INT,
            DimensionType::height,
            "logical_height",
            Codec.INT,
            DimensionType::logicalHeight,
            "infiniburn",
            Codec.STRING,
            DimensionType::infiniburn,
            "ambient_light",
            Codec.FLOAT,
            DimensionType::ambientLight,
            "monster_spawn_light_level", MonsterSpawnLightLevel.CODEC, DimensionType::monsterSpawnLightLevel,
            "monster_spawn_block_light_limit",
            Codec.INT,
            DimensionType::monsterSpawnBlockLightLimit,
//            "skybox", DimensionType.Skybox.CODEC.optional(DimensionType.Skybox.OVERWORLD), DimensionType::skybox,
//            "cardinal_light", DimensionType.CardinalLight.CODEC.optional(DimensionType.CardinalLight.DEFAULT), DimensionType::cardinalLight,
            "attributes",
            EnvironmentAttributeMap.CODEC.default(EnvironmentAttributeMap.EMPTY),
            DimensionType::attributes,
//            "timelines", RegistryTag.codec(Registries::timeline).optional(RegistryTag.empty()), DimensionType::timelines,
            ::DimensionType
        )
    }

    sealed interface MonsterSpawnLightLevel {
        data class Uniform(
            val maxInclusive: Int,
            val minInclusive: Int,
        ) : MonsterSpawnLightLevel {
            companion object {
                val CODEC = StructCodec.of(
                    "type", Codec.KEY, { "uniform".toKey() },
                    "max_inclusive", Codec.INT, Uniform::maxInclusive,
                    "min_inclusive", Codec.INT, Uniform::minInclusive,
                    { _, maxInclusive, minInclusive -> Uniform(maxInclusive, minInclusive) }
                )
            }
        }

        data class Constant(
            val value: Int,
        ) : MonsterSpawnLightLevel {
            companion object {
                val CODEC = Codec.INT.transform(::Constant, Constant::value)
            }
        }

        companion object {
            val CODEC = object : Codec<MonsterSpawnLightLevel> {
                override fun <D> encode(transcoder: Transcoder<D>, value: MonsterSpawnLightLevel): D {
                    return when (value) {
                        is Constant -> Constant.CODEC.encode(transcoder, value)
                        is Uniform -> Uniform.CODEC.encode(transcoder, value)
                    }
                }

                override fun <D> decode(transcoder: Transcoder<D>, value: D): MonsterSpawnLightLevel {
                    val mapResult = runCatching { transcoder.decodeMap(value) }
                    val type = if (mapResult.isSuccess) {
                        Codec.KEY.decode(transcoder, mapResult.getOrThrow().getValue("type")).toString()
                    } else {
                        null
                    }

                    val codec = when (type) {
                        null -> Constant.CODEC
                        "minecraft:uniform" -> Uniform.CODEC
                        else -> throw IllegalStateException("Unknown type $type")
                    }

                    return codec.decode(transcoder, value)
                }
            }
        }
    }
}