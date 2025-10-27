package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.minecraft.protocol.types.DyeColor

@Suppress("PropertyName")
sealed class TropicalFishMeta : AbstractFishMeta() {
    companion object : TropicalFishMeta()

    val VARIANT = index(
        MetadataEntry.Type.VAR_INT,
        Variant.DEFAULT,
        Variant::fromInt,
        Variant::toInt
    )

    data class Variant(
        val pattern: Pattern,
        val baseColor: DyeColor,
        val patternColor: DyeColor,
    ) {
        companion object {
            val DEFAULT = Variant(Pattern.KOB, DyeColor.WHITE, DyeColor.WHITE)
            fun toInt(data: Variant): Int {
                return (data.pattern.ordinal and 0xFFFF) or
                        ((data.baseColor.ordinal and 0xFF) shl 16) or
                        ((data.patternColor.ordinal and 0xFF) shl 24)
            }

            fun fromInt(value: Int): Variant {
                return Variant(
                    Pattern.entries[value and 0xFFFF],
                    DyeColor.entries[(value shr 16) and 0xFF],
                    DyeColor.entries[(value shr 24) and 0xFF]
                )
            }
        }
    }

    enum class Pattern {
        KOB,
        SUNSTREAK,
        SNOOPER,
        DASHER,
        BRINELY,
        SPOTTY,
        FLOPPER,
        STRIPEY,
        GLITTER,
        BLOCKFISH,
        BETTY,
        CLAYFISH;
    }
}