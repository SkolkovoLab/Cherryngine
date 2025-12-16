package ru.cherryngine.lib.minecraft.envattr

import ru.cherryngine.lib.minecraft.codec.Codec

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