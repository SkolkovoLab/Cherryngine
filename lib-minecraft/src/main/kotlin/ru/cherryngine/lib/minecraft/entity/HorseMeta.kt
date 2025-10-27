package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class HorseMeta : AbstractHorse() {
    companion object : HorseMeta()

    val VARIANT = index(
        MetadataEntry.Type.VAR_INT,
        Data.DEFAULT,
        Data::fromInt,
        Data::toInt
    )

    data class Data(
        val variant: Variant,
        val markings: Markings,
    ) {
        companion object {
            val DEFAULT = Data(Variant.WHITE, Markings.NONE)
            fun toInt(data: Data): Int {
                return (data.variant.ordinal and 0xFF) or ((data.markings.ordinal shl 8) and 0xFF00)
            }

            fun fromInt(value: Int): Data {
                return Data(
                    Variant.entries[value and 0xFF],
                    Markings.entries[(value and 0xFF00) shr 8]
                )
            }
        }
    }

    enum class Variant {
        WHITE,
        CREAMY,
        CHESTNUT,
        BROWN,
        BLACK,
        GRAY,
        DARK_BROWN;
    }

    enum class Markings {
        NONE,
        WHITE,
        WHITE_FIELD,
        WHITE_DOTS,
        BLACK_DOTS;
    }
}