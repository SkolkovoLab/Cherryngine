package ru.cherryngine.lib.minecraft.registry.registries.envattr

import ru.cherryngine.lib.minecraft.tide.codec.Codec
import ru.cherryngine.lib.minecraft.tide.codec.StructCodec

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