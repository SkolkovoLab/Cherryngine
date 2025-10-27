package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.math.Vec3D

@Suppress("PropertyName")
sealed class ArmorStandMeta : LivingEntityMeta() {
    companion object : ArmorStandMeta()

    val ARMOR_STAND_FLAGS = index(
        MetadataEntry.Type.BYTE,
        Flags.DEFAULT,
        Flags::fromByte,
        Flags::toByte
    )
    val HEAD_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D.ZERO)
    val BODY_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D.ZERO)
    val LEFT_ARM_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(-10.0, 0.0, -10.0))
    val RIGHT_ARM_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(-15.0, 0.0, 10.0))
    val LEFT_LEG_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(-1.0, 0.0, -1.0))
    val RIGHT_LEG_ROTATION = index(MetadataEntry.Type.ROTATION, Vec3D(1.0, 0.0, 1.0))

    data class Flags(
        val isSmall: Boolean = false,
        val hasArms: Boolean = false,
        val noBasePlate: Boolean = false,
        val isMarker: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
                var byte = 0
                if (flags.isSmall) byte = byte or 0x01
                if (flags.hasArms) byte = byte or 0x04
                if (flags.noBasePlate) byte = byte or 0x08
                if (flags.isMarker) byte = byte or 0x10
                return byte.toByte()
            }

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
                    isSmall = (byte and 0x01) != 0,
                    hasArms = (byte and 0x04) != 0,
                    noBasePlate = (byte and 0x08) != 0,
                    isMarker = (byte and 0x10) != 0,
                )
            }
        }
    }
}