package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.KeyPattern
import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.utils.color.ARGBLikeImpl
import ru.cherryngine.lib.minecraft.utils.color.RGBLikeImpl

data class EnvironmentAttributeType<T>(
    val key: Key,
    val codec: Codec<T>,
    val modifierCodec: Codec<Modifier<T, *>>,
) {

    companion object {
        val BOOLEAN = register("boolean", Codec.BOOLEAN, Modifier.BOOLEAN_OPERATORS)
        val TRI_STATE = register("tri_state", TRI_STATE_CODEC, mapOf())
        val FLOAT = register("float", Codec.FLOAT, Modifier.FLOAT_OPERATORS)
        val ANGLE_DEGREES = register("angle_degrees", Codec.FLOAT, Modifier.FLOAT_OPERATORS)
        val RGB_COLOR = register("rgb_color", RGBLikeImpl.STRING_CODEC, Modifier.RGB_OPERATORS)
        val ARGB_COLOR = register("argb_color", ARGBLikeImpl.STRING_CODEC, Modifier.ARGB_OPERATORS)
        val MOON_PHASE = register("moon_phase", MoonPhase.CODEC, mapOf())
        val ACTIVITY = register("activity", EntityActivity.CODEC, mapOf())
        val BED_RULE = register("bed_rule", BedRule.CODEC, mapOf())
        val PARTICLE = register("particle", TMP_PARTICLE_CODEC, mapOf())
        val AMBIENT_PARTICLES = register("ambient_particles", AmbientParticle.CODEC.list(), mapOf())
        val BACKGROUND_MUSIC = register("background_music", BackgroundMusic.CODEC, mapOf())
        val AMBIENT_SOUNDS = register("ambient_sounds", AmbientSounds.CODEC, mapOf())

        fun <T> register(
            @KeyPattern key: String,
            codec: Codec<T>,
            operators: Map<Modifier.Operator, Modifier<T, *>>,
        ): EnvironmentAttributeType<T> {

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

            return EnvironmentAttributeType(
                Key.key(key),
                codec,
                modifierCodec
            )
        }
    }
}