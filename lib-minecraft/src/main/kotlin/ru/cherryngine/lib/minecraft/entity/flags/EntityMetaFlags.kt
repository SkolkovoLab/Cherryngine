package ru.cherryngine.lib.minecraft.entity.flags

data class EntityMetaFlags(
    val isOnFire: Boolean = false,
    val isSneaking: Boolean = false, // hides the name tag, but does not make the entity visually sneak
    val isSprinting: Boolean = false, // shows sprinting particles when on ground
    val isSwimming: Boolean = false,
    val isInvisible: Boolean = false,
    val hasGlowingEffects: Boolean = false,
    val isGliding: Boolean = false,
) {
    companion object {
        val DEFAULT = EntityMetaFlags()

        fun toByte(flags: EntityMetaFlags): Byte {
            var byte = 0
            if (flags.isOnFire) byte = byte or 0x01
            if (flags.isSneaking) byte = byte or 0x02
            if (flags.isSprinting) byte = byte or 0x08
            if (flags.isSwimming) byte = byte or 0x10
            if (flags.isInvisible) byte = byte or 0x20
            if (flags.hasGlowingEffects) byte = byte or 0x40
            if (flags.isGliding) byte = byte or 0x80
            return byte.toByte()
        }

        fun fromByte(byte: Byte): EntityMetaFlags {
            val byte = byte.toInt()
            return EntityMetaFlags(
                isOnFire = (byte and 0x01) != 0,
                isSneaking = (byte and 0x02) != 0,
                isSprinting = (byte and 0x08) != 0,
                isSwimming = (byte and 0x10) != 0,
                isInvisible = (byte and 0x20) != 0,
                hasGlowingEffects = (byte and 0x40) != 0,
                isGliding = (byte and 0x80) != 0,
            )
        }
    }
}