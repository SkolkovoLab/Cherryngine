package ru.cherryngine.lib.minecraft.registry.registries

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import net.kyori.adventure.key.Keyed
import net.kyori.adventure.util.ARGBLike
import net.kyori.adventure.util.RGBLike
import net.kyori.adventure.util.TriState
import ru.cherryngine.lib.minecraft.protocol.types.SoundEvent
import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.registry.entries.Particle
import ru.cherryngine.lib.minecraft.registry.keys.Particles
import ru.cherryngine.lib.minecraft.registry.keys.Sounds
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.EitherCodec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec
import ru.cherryngine.lib.minecraft.tide.codec.TypedMapCodec
import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.tide.types.Either
import ru.cherryngine.lib.minecraft.utils.AlphaColor
import ru.cherryngine.lib.minecraft.utils.Color
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.max
import kotlin.math.min

// TODO тут полный срач, но время уже 6 утра нахуй, потом раскидаю

object EnvironmentAttributeRegistry : DynamicRegistry<EnvironmentAttribute<*>>("minecraft:environment_attribute") {
    init {
        register("visual/fog_color", Type.RGB_COLOR, Color.BLACK)
        register("visual/fog_start_distance", Type.FLOAT, 0f)
        register("visual/fog_end_distance", Type.FLOAT, 1024f)
        register("visual/sky_fog_end_distance", Type.FLOAT, 512f)
        register("visual/cloud_fog_end_distance", Type.FLOAT, 2048f)
        register("visual/water_fog_color", Type.RGB_COLOR, Color(0x050533))
        register("visual/water_fog_start_distance", Type.FLOAT, -8f)
        register("visual/water_fog_end_distance", Type.FLOAT, 96f)
        register("visual/sky_color", Type.RGB_COLOR, Color.BLACK)
        register("visual/sunrise_sunset_color", Type.ARGB_COLOR, AlphaColor.TRANSPARENT)
        register("visual/cloud_color", Type.ARGB_COLOR, AlphaColor.TRANSPARENT)
        register("visual/cloud_height", Type.FLOAT, 192.33f)
        register("visual/sun_angle", Type.ANGLE_DEGREES, 0f)
        register("visual/moon_angle", Type.ANGLE_DEGREES, 0f)
        register("visual/star_angle", Type.ANGLE_DEGREES, 0f)
        register("visual/moon_phase", Type.MOON_PHASE, MoonPhase.FULL_MOON)
        register("visual/star_brightness", Type.FLOAT, 0f)
        register("visual/sky_light_color", Type.RGB_COLOR, Color.WHITE)
        register("visual/sky_light_factor", Type.FLOAT, 1f)
        register("visual/default_dripstone_particle", Type.PARTICLE, Particles.DRIPPING_DRIPSTONE_WATER)
        register("visual/ambient_particles", Type.AMBIENT_PARTICLES, emptyList())
        register("audio/background_music", Type.BACKGROUND_MUSIC, BackgroundMusic.EMPTY)
        register("audio/music_volume", Type.FLOAT, 1f)
        register("audio/ambient_sounds", Type.AMBIENT_SOUNDS, AmbientSounds.EMPTY)
        register("audio/firefly_bush_sounds", Type.BOOLEAN, false)
        register("gameplay/sky_light_level", Type.FLOAT, 15f)
        register("gameplay/can_start_raid", Type.BOOLEAN, true)
        register("gameplay/water_evaporates", Type.BOOLEAN, false)
        register("gameplay/bed_rule", Type.BED_RULE, BedRule.CAN_SLEEP_WHEN_DARK)
        register("gameplay/respawn_anchor_works", Type.BOOLEAN, false)
        register("gameplay/nether_portal_spawns_piglin", Type.BOOLEAN, false)
        register("gameplay/fast_lava", Type.BOOLEAN, false)
        register("gameplay/increased_fire_burnout", Type.BOOLEAN, false)
        register("gameplay/eyeblossom_open", Type.TRI_STATE, TriState.NOT_SET)
        register("gameplay/turtle_egg_hatch_chance", Type.FLOAT, 0f)
        register("gameplay/piglins_zombify", Type.BOOLEAN, true)
        register("gameplay/snow_golem_melts", Type.BOOLEAN, false)
        register("gameplay/creaking_active", Type.BOOLEAN, false)
        register("gameplay/surface_slime_spawn_chance", Type.FLOAT, 0f)
        register("gameplay/cat_waking_up_gift_chance", Type.FLOAT, 0f)
        register("gameplay/bees_stay_in_hive", Type.BOOLEAN, false)
        register("gameplay/monsters_burn", Type.BOOLEAN, false)
        register("gameplay/can_pillager_patrol_spawn", Type.BOOLEAN, true)
        register("gameplay/villager_activity", Type.ACTIVITY, EntityActivity.IDLE)
        register("gameplay/baby_villager_activity", Type.ACTIVITY, EntityActivity.IDLE)
        updateCache()
    }

    fun <T> register(identifier: String, type: Type<T>, default: T) {
        addEntry(EnvironmentAttribute("minecraft:$identifier", type, default))
    }
}

class EnvironmentAttributeMap(
    val entries: Map<EnvironmentAttribute<*>, Entry<*, *>>,
) {
    companion object {
        val EMPTY = EnvironmentAttributeMap(mapOf())

        val CODEC = TypedMapCodec(
            EnvironmentAttribute.CODEC,
            { Entry.codec0(it) },
            Int.MAX_VALUE,
            ConcurrentHashMap()
        ).transform(::EnvironmentAttributeMap, EnvironmentAttributeMap::entries)
    }
}

data class Entry<T, Arg>(
    val argument: Arg,
    val modifier: Modifier<T, Arg>,
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T> codec(attribute: EnvironmentAttribute<T>): Codec<Entry<T, *>> {
            // A value is represented by either a single value which acts as an override,
            // or a struct with `modifier` and `argument` keys (full codec).

            val fullCodec: Codec<Entry<T, *>> = attribute.type.modifierCodec
                .union(
                    "modifier",
                    attribute.type.modifierCodec,
                    { modifier -> fullCodec(modifier) },
                    { it.modifier }
                )

            val override = Modifier.Override(attribute.type.codec)
            return EitherCodec(attribute.type.codec, fullCodec).transform(
                { either ->
                    either.unify(
                        { value -> Entry(value, override) },
                        { u -> u }
                    )
                },
                { entry ->
                    if (entry.modifier is Modifier.Override<*>)
                        Either.Left(entry.argument as T)
                    else
                        Either.Right(entry)
                })
        }

        @Suppress("UNCHECKED_CAST")
        fun codec0(attribute: EnvironmentAttribute<*>): Codec<Entry<*, *>> {
            return codec(attribute) as Codec<Entry<*, *>>
        }

        private fun <T, Arg> fullCodec(modifier: Modifier<T, Arg>): StructCodec<Entry<T, Arg>> {
            return StructCodec.of(
                "argument", modifier.argumentCodec(), { it.argument },
                { argument -> Entry(argument, modifier) }
            )
        }
    }
}

data class EnvironmentAttribute<T>(
    val identifier: String,
    val type: Type<T>,
    val default: T,
) : RegistryEntry {
    override fun getEntryIdentifier() = identifier

    companion object {
        val CODEC = Codec.KEY.transform(
            { EnvironmentAttributeRegistry[it] },
            { Key.key(it.identifier) }
        )
    }
}

data class Type<T>(
    val key: Key,
    val codec: Codec<T>,
    val modifierCodec: Codec<Modifier<T, *>>,
) {

    companion object {
        val BOOLEAN = register("boolean", Codec.BOOLEAN, Modifier.BOOLEAN_OPERATORS)
        val TRI_STATE = register("tri_state", TriStateCodec, mapOf())
        val FLOAT = register("float", Codec.FLOAT, Modifier.FLOAT_OPERATORS)
        val ANGLE_DEGREES = register("angle_degrees", Codec.FLOAT, Modifier.FLOAT_OPERATORS)
        val RGB_COLOR = register("rgb_color", Color.STRING_CODEC, Modifier.RGB_OPERATORS)
        val ARGB_COLOR = register("argb_color", AlphaColor.STRING_CODEC, Modifier.ARGB_OPERATORS)
        val MOON_PHASE = register("moon_phase", MoonPhase.CODEC, mapOf())
        val ACTIVITY: Type<EntityActivity> = register("activity", EntityActivity.CODEC, mapOf())
        val BED_RULE: Type<BedRule> = register("bed_rule", BedRule.CODEC, mapOf())
        val PARTICLE = register("particle", TMP_PARTICLE_CODEC, mapOf())
        val AMBIENT_PARTICLES = register("ambient_particles", AmbientParticle.CODEC.list(), mapOf())
        val BACKGROUND_MUSIC = register("background_music", BackgroundMusic.CODEC, mapOf())
        val AMBIENT_SOUNDS = register("ambient_sounds", AmbientSounds.CODEC, mapOf())

        fun <T> register(
            @KeyPattern key: String,
            codec: Codec<T>,
            operators: Map<Modifier.Operator, Modifier<T, *>>,
        ): Type<T> {

            // include OVERRIDE operator
            val withOverride = HashMap(operators).apply {
                put(Modifier.Operator.OVERRIDE, Modifier.Override(codec))
            }

            // inverse mapping: Modifier -> Operator
            val inverse = HashMap<Modifier<T, *>, Modifier.Operator>(operators.size).apply {
                for ((op, mod) in operators) {
                    put(mod, op)
                }
            }

            // codec for modifiers
            val modifierCodec: Codec<Modifier<T, *>> =
                Modifier.Operator.CODEC.transform(
                    { op -> withOverride[op]!! },
                    { mod -> inverse[mod]!! }
                )

            return Type(
                Key.key(key),
                codec,
                modifierCodec
            )
        }
    }
}

enum class MoonPhase {
    FULL_MOON,
    WANING_GIBBOUS,
    THIRD_QUARTER,
    WANING_CRESCENT,
    NEW_MOON,
    WAXING_CRESCENT,
    FIRST_QUARTER,
    WAXING_GIBBOUS;

    companion object {
        val CODEC: Codec<MoonPhase> = Codec.enum<MoonPhase>()
    }
}

enum class EntityActivity : Keyed {
    CORE,
    IDLE,
    WORK,
    PLAY,
    REST,
    MEET,
    PANIC,
    RAID,
    PRE_RAID,
    HIDE,
    FIGHT,
    CELEBRATE,
    ADMIRE_ITEM,
    AVOID,
    RIDE,
    PLAY_DEAD,
    LONG_JUMP,
    RAM,
    TONGUE,
    SWIM,
    LAY_SPAWN,
    SNIFF,
    INVESTIGATE,
    ROAR,
    EMERGE,
    DIG;

    private val key = Key.key(name.lowercase())
    override fun key(): Key = key

    companion object {
        private val BY_KEY = entries.associateBy { it.key }

        fun byKey(key: Key) = BY_KEY[key]!!

        val CODEC: Codec<EntityActivity> = Codec.KEY.transform(::byKey, Keyed::key)
    }
}

@JvmRecord
data class BedRule(
    val canSleep: Rule,
    val canSetSpawn: Rule,
    val explodes: Boolean,
    val errorMessage: String?,
) {
    enum class Rule {
        ALWAYS,
        WHEN_DARK,
        NEVER;

        companion object {
            val CODEC = Codec.enum<Rule>()
        }
    }

    companion object {
        /** The default vanilla overworld bed behavior. */
        val CAN_SLEEP_WHEN_DARK: BedRule = BedRule(
            Rule.WHEN_DARK, Rule.ALWAYS,
            false, "block.minecraft.bed.no_sleep"
        )

        /** THe default vanilla nether/end bed behavior. */
        val EXPLODES: BedRule = BedRule(Rule.NEVER, Rule.NEVER, true, null)

        val CODEC: Codec<BedRule> = StructCodec.of(
            "can_sleep", Rule.CODEC, BedRule::canSleep,
            "can_set_spawn", Rule.CODEC, BedRule::canSetSpawn,
            "explodes", Codec.BOOLEAN.default(false), BedRule::explodes,
            "error_message", Codec.STRING.optional(), BedRule::errorMessage,
            ::BedRule
        )
    }
}


object TriStateCodec : Codec<TriState> {
    override fun <D> decode(transcoder: Transcoder<D>, value: D): TriState {
        // Try boolean first
        val boolResult = runCatching { transcoder.decodeBoolean(value) }
        if (boolResult.isSuccess) return TriState.byBoolean(boolResult.getOrThrow())

        // Then try string
        return when (val string = transcoder.decodeString(value).lowercase()) {
            "true" -> TriState.TRUE
            "false" -> TriState.FALSE
            "default" -> TriState.NOT_SET
            else -> throw IllegalArgumentException("expected true, false, or \"default\", got: $string")
        }
    }

    override fun <D> encode(
        transcoder: Transcoder<D>,
        value: TriState,
    ): D {
        return when (value) {
            TriState.TRUE -> transcoder.encodeBoolean(true)
            TriState.FALSE -> transcoder.encodeBoolean(false)
            TriState.NOT_SET -> transcoder.encodeString("default")
        }
    }
}


@Suppress("UNCHECKED_CAST")
sealed interface Modifier<Sub, Arg> {

    companion object {
        val BOOLEAN_OPERATORS: Map<Operator, Modifier<Boolean, *>> = mapOf(
            Operator.AND to BooleanMod.AND,
            Operator.NAND to BooleanMod.NAND,
            Operator.OR to BooleanMod.OR,
            Operator.NOR to BooleanMod.NOR,
            Operator.XOR to BooleanMod.XOR,
            Operator.XNOR to BooleanMod.XNOR,
        )

        val FLOAT_OPERATORS: Map<Operator, Modifier<Float, *>> = mapOf(
            Operator.ALPHA_BLEND to FloatMod.ALPHA_BLEND,
            Operator.ADD to FloatMod.ADD,
            Operator.SUBTRACT to FloatMod.SUBTRACT,
            Operator.MULTIPLY to FloatMod.MULTIPLY,
            Operator.MAXIMUM to FloatMod.MAXIMUM,
            Operator.MINIMUM to FloatMod.MINIMUM,
        )

        val RGB_OPERATORS: Map<Operator, Modifier<RGBLike, *>> = mapOf(
            Operator.ALPHA_BLEND to ColorMod.ALPHA_BLEND,
            Operator.ADD to ColorMod.ADD,
            Operator.SUBTRACT to ColorMod.SUBTRACT,
            Operator.MULTIPLY to ColorMod.MULTIPLY_RGB,
            Operator.BLEND_TO_GRAY to ColorMod.BLEND_TO_GRAY,
        )

        val ARGB_OPERATORS: Map<Operator, Modifier<ARGBLike, *>> = mapOf(
            Operator.ALPHA_BLEND to ColorMod.ALPHA_BLEND as Modifier<ARGBLike, *>,
            Operator.ADD to ColorMod.ADD as Modifier<ARGBLike, *>,
            Operator.SUBTRACT to ColorMod.SUBTRACT as Modifier<ARGBLike, *>,
            Operator.MULTIPLY to ColorMod.MULTIPLY_ARGB as Modifier<ARGBLike, *>,
            Operator.BLEND_TO_GRAY to ColorMod.BLEND_TO_GRAY as Modifier<ARGBLike, *>,
        )
    }

    enum class Operator {
        OVERRIDE,
        ALPHA_BLEND,
        ADD,
        SUBTRACT,
        MULTIPLY,
        BLEND_TO_GRAY,
        MINIMUM,
        MAXIMUM,
        AND,
        NAND,
        OR,
        NOR,
        XOR,
        XNOR;

        companion object {
            val CODEC: Codec<Operator> = Codec.enum<Operator>()
        }
    }

    data class Override<Value>(
        val argumentCodecValue: Codec<Value>,
    ) : Modifier<Value, Value> {
        override fun modify(subject: Value, argument: Value): Value = argument
        override fun argumentCodec(): Codec<Value> = argumentCodecValue
    }

    object BooleanMod {
        val AND: Modifier<Boolean, Boolean> = BooleanModifier.AND
        val NAND: Modifier<Boolean, Boolean> = BooleanModifier.NAND
        val OR: Modifier<Boolean, Boolean> = BooleanModifier.OR
        val NOR: Modifier<Boolean, Boolean> = BooleanModifier.NOR
        val XOR: Modifier<Boolean, Boolean> = BooleanModifier.XOR
        val XNOR: Modifier<Boolean, Boolean> = BooleanModifier.XNOR
    }

    object FloatMod {
        val ALPHA_BLEND: Modifier<Float, AlphaFloat> = FloatModifier.ALPHA_BLEND
        val ADD: Modifier<Float, Float> = FloatModifier.ADD
        val SUBTRACT: Modifier<Float, Float> = FloatModifier.SUBTRACT
        val MULTIPLY: Modifier<Float, Float> = FloatModifier.MULTIPLY
        val MAXIMUM: Modifier<Float, Float> = FloatModifier.MAXIMUM
        val MINIMUM: Modifier<Float, Float> = FloatModifier.MINIMUM
    }

    object ColorMod {
        val ALPHA_BLEND: Modifier<RGBLike, ARGBLike> = ColorModifier.ALPHA_BLEND
        val ADD: Modifier<RGBLike, RGBLike> = ColorModifier.ADD
        val SUBTRACT: Modifier<RGBLike, RGBLike> = ColorModifier.SUBTRACT
        val MULTIPLY_RGB: Modifier<RGBLike, RGBLike> = ColorModifier.MULTIPLY_RGB
        val MULTIPLY_ARGB: Modifier<RGBLike, ARGBLike> = ColorModifier.MULTIPLY_ARGB
        val BLEND_TO_GRAY: Modifier<RGBLike, BlendToGray> = ColorModifier.BLEND_TO_GRAY
    }

    fun modify(subject: Sub, argument: Arg): Sub
    fun argumentCodec(): Codec<Arg>
}

fun interface BooleanModifier : Modifier<Boolean, Boolean> {

    override fun modify(subject: Boolean, argument: Boolean): Boolean

    override fun argumentCodec(): Codec<Boolean> = Codec.BOOLEAN

    companion object {
        val AND = BooleanModifier { a, b -> a && b }
        val NAND = BooleanModifier { a, b -> !a || !b }
        val OR = BooleanModifier { a, b -> a || b }
        val NOR = BooleanModifier { a, b -> !a && !b }
        val XOR = BooleanModifier { a, b -> a.xor(b) }
        val XNOR = BooleanModifier { a, b -> a == b }
    }
}


interface FloatModifier<Arg> : Modifier<Float, Arg> {
    companion object {
        val ALPHA_BLEND: FloatModifier<AlphaFloat> =
            object : FloatModifier<AlphaFloat> {
                override fun modify(sub: Float, arg: AlphaFloat): Float {
                    return sub + arg.alpha * (arg.value - sub)
                }

                override fun argumentCodec(): Codec<AlphaFloat> =
                    AlphaFloat.CODEC
            }

        val ADD: ToFloat = ToFloat { x, y -> x + y }
        val SUBTRACT: ToFloat = ToFloat { x, y -> x - y }
        val MULTIPLY: ToFloat = ToFloat { x, y -> x * y }
        val MINIMUM: ToFloat = ToFloat { x, y -> min(x, y) }
        val MAXIMUM: ToFloat = ToFloat { x, y -> max(x, y) }
    }

    fun interface ToFloat : FloatModifier<Float> {
        override fun modify(subject: Float, argument: Float): Float

        override fun argumentCodec(): Codec<Float> = Codec.FLOAT
    }
}

data class AlphaFloat(
    val value: Float,
    val alpha: Float = 1f,
) {
    companion object {
        private val STRUCT_CODEC: StructCodec<AlphaFloat> =
            StructCodec.of(
                "value", Codec.FLOAT, AlphaFloat::value,
                "alpha", Codec.FLOAT.default(1f), AlphaFloat::alpha,
                ::AlphaFloat
            )

        val CODEC: Codec<AlphaFloat> =
            EitherCodec(Codec.FLOAT, STRUCT_CODEC).transform(
                { either ->
                    either.unify(
                        { v -> AlphaFloat(v, 1f) },
                        { af -> af }
                    )
                },
                { af ->
                    if (af.alpha == 1f)
                        Either.Left(af.value)
                    else
                        Either.Right(af)
                }
            )
    }
}

interface ColorModifier<Arg> : Modifier<RGBLike, Arg> {

    companion object {
        val MAYBE_ARGB_CODEC: Codec<RGBLike> =
            EitherCodec(AlphaColor.STRING_CODEC, Color.STRING_CODEC)
                .transform(
                    { either ->
                        either.unify({ it }, { it })
                    },
                    { color ->
                        if (color is ARGBLike && color.alpha() != 255)
                            Either.Left(color)
                        else
                            Either.Right(color)
                    }
                )

        val ALPHA_BLEND: ColorModifier<ARGBLike> =
            object : ColorModifier<ARGBLike> {
                override fun modify(subject: RGBLike, argument: ARGBLike): RGBLike {
                    throw UnsupportedOperationException("alpha blend is not implemented yet")
                }

                override fun argumentCodec(): Codec<ARGBLike> =
                    AlphaColor.STRING_CODEC
            }

        val ADD: ColorModifier<RGBLike> =
            object : ColorModifier<RGBLike> {
                override fun modify(subject: RGBLike, argument: RGBLike): RGBLike {
                    val alpha = (subject as? ARGBLike)?.alpha() ?: 255
                    return AlphaColor(
                        alpha,
                        min(255, subject.red() + argument.red()),
                        min(255, subject.green() + argument.green()),
                        min(255, subject.blue() + argument.blue())
                    )
                }

                override fun argumentCodec(): Codec<RGBLike> =
                    MAYBE_ARGB_CODEC
            }

        val SUBTRACT: ColorModifier<RGBLike> =
            object : ColorModifier<RGBLike> {
                override fun modify(subject: RGBLike, argument: RGBLike): RGBLike {
                    val alpha = (subject as? ARGBLike)?.alpha() ?: 255
                    return AlphaColor(
                        alpha,
                        max(0, subject.red() - argument.red()),
                        max(0, subject.green() - argument.green()),
                        max(0, subject.blue() - argument.blue())
                    )
                }

                override fun argumentCodec(): Codec<RGBLike> =
                    MAYBE_ARGB_CODEC
            }

        val MULTIPLY_RGB: ColorModifier<RGBLike> =
            object : ColorModifier<RGBLike> {
                override fun modify(subject: RGBLike, argument: RGBLike): RGBLike {
                    val subA = (subject as? ARGBLike)?.alpha() ?: 255
                    val argA = (argument as? ARGBLike)?.alpha() ?: 255
                    return AlphaColor(
                        (subA * argA) / 255,
                        (subject.red() * argument.red()) / 255,
                        (subject.green() * argument.green()) / 255,
                        (subject.blue() * argument.blue()) / 255,
                    )
                }

                override fun argumentCodec(): Codec<RGBLike> =
                    Color.STRING_CODEC
            }

        val MULTIPLY_ARGB: ColorModifier<ARGBLike> =
            object : ColorModifier<ARGBLike> {
                override fun modify(subject: RGBLike, argument: ARGBLike): RGBLike {
                    val subA = (subject as? ARGBLike)?.alpha() ?: 255
                    val argA = argument.alpha()
                    return AlphaColor(
                        (subA * argA) / 255,
                        (subject.red() * argument.red()) / 255,
                        (subject.green() * argument.green()) / 255,
                        (subject.blue() * argument.blue()) / 255,
                    )
                }

                override fun argumentCodec(): Codec<ARGBLike> =
                    AlphaColor.STRING_CODEC
            }

        val BLEND_TO_GRAY: ColorModifier<BlendToGray> =
            object : ColorModifier<BlendToGray> {
                override fun modify(subject: RGBLike, argument: BlendToGray): RGBLike {
                    throw UnsupportedOperationException("blend to gray is not implemented yet")
                }

                override fun argumentCodec(): Codec<BlendToGray> =
                    BlendToGray.CODEC
            }
    }
}

data class BlendToGray(
    val brightness: Float,
    val factor: Float,
) {
    companion object {
        val CODEC: Codec<BlendToGray> = StructCodec.of(
            "brightness", Codec.FLOAT, BlendToGray::brightness,
            "factor", Codec.FLOAT, BlendToGray::factor,
            ::BlendToGray
        )
    }
}

class BackgroundMusic(
    val music: Music?,
    val creativeMusic: Music?,
    val underwaterMusic: Music?,
) {
    companion object {
        val EMPTY: BackgroundMusic = BackgroundMusic(null, null, null)
        val OVERWORLD: BackgroundMusic = BackgroundMusic(Music.GAME, Music.CREATIVE, null)

        val CODEC: Codec<BackgroundMusic> = StructCodec.of(
            "music", Music.CODEC.optional(), BackgroundMusic::music,
            "creative_music", Music.CODEC.optional(), BackgroundMusic::creativeMusic,
            "underwater_music", Music.CODEC.optional(), BackgroundMusic::underwaterMusic,
            ::BackgroundMusic
        )
    }
}

data class Music(
    val sound: Key,
    val minDelay: Int,
    val maxDelay: Int,
    val replaceCurrentMusic: Boolean,
) {
    companion object {
        val MENU: Music = Music(Sounds.MUSIC_MENU, 20, 600, true)
        val CREATIVE: Music = Music(Sounds.MUSIC_CREATIVE, 12000, 24000, false)
        val CREDITS: Music = Music(Sounds.MUSIC_CREDITS, 0, 0, true)
        val END_BOSS: Music = Music(Sounds.MUSIC_DRAGON, 0, 0, true)
        val END: Music = Music(Sounds.MUSIC_END, 6000, 24000, true)
        val UNDER_WATER: Music = Music(Sounds.MUSIC_UNDER_WATER, 12000, 24000, false)
        val GAME: Music = Music(Sounds.MUSIC_GAME, 12000, 24000, false)

        val CODEC: Codec<Music> = StructCodec.of(
            "sound", Codec.KEY, Music::sound,
            "min_delay", Codec.INT, Music::minDelay,
            "max_delay", Codec.INT, Music::maxDelay,
            "replace_current_music", Codec.BOOLEAN, Music::replaceCurrentMusic,
            ::Music
        )
    }
}

data class AmbientSounds(
    val loop: SoundEvent?,
    val mood: Mood?,
    val additions: List<Additions>,
) {
    data class Mood(
        val sound: SoundEvent,
        val tickDelay: Int,
        val blockSearchExtent: Int,
        val offset: Double,
    ) {
        companion object {
            val CODEC: Codec<Mood> = StructCodec.of(
                "sound", SoundEvent.CODEC, Mood::sound,
                "tick_delay", Codec.INT, Mood::tickDelay,
                "block_search_extent", Codec.INT, Mood::blockSearchExtent,
                "offset", Codec.DOUBLE, Mood::offset,
                ::Mood
            )
        }
    }

    data class Additions(
        val sound: SoundEvent,
        val tickChance: Double,
    ) {
        companion object {
            val CODEC: Codec<Additions> = StructCodec.of(
                "sound", SoundEvent.CODEC, Additions::sound,
                "tick_chance", Codec.DOUBLE, Additions::tickChance,
                ::Additions
            )
        }
    }

    companion object {
        val EMPTY: AmbientSounds = AmbientSounds(null, null, listOf())

        val CODEC: Codec<AmbientSounds> = StructCodec.of(
            "loop", SoundEvent.CODEC.optional(), AmbientSounds::loop,
            "mood", Mood.CODEC.optional(), AmbientSounds::mood,
            "additions", Additions.CODEC.listOrSingle().default(listOf()), AmbientSounds::additions,
            ::AmbientSounds
        )
    }
}

data class TmpParticle(
    val type: String,
) {
    companion object {
        val CODEC = StructCodec.of(
            "type", Codec.STRING, TmpParticle::type,
            ::TmpParticle
        )
    }
}

val TMP_PARTICLE_CODEC = StructCodec.of(
    "type", Codec.KEY, { Key.key(it.identifier) },
    { ParticleRegistry[it] }
)

class AmbientParticle(
    val particle: Particle,
    val probability: Float,
) {
    companion object {
        val CODEC = StructCodec.of(
            "particle", TMP_PARTICLE_CODEC, AmbientParticle::particle,
            "probability", Codec.FLOAT, AmbientParticle::probability,
            ::AmbientParticle
        )
    }
}

