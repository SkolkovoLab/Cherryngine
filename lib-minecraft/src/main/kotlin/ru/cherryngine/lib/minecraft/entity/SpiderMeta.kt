package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class SpiderMeta : MobMeta() {
    companion object : SpiderMeta()

    val SPIDER_FLAGS = index(
            MetadataEntry.Type.BYTE,
            Flags.DEFAULT,
            Flags::fromByte,
            Flags::toByte
        )

    data class Flags(
        val isClimbing: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isClimbing) byte = byte or 0x01
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isClimbing = (byte and 0x01) != 0,
                )
            }
        }
    }
}