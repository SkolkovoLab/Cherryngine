package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class EntityMeta : MetadataDef() {
    companion object : EntityMeta()

    val ENTITY_FLAGS = index(
            MetadataEntry.Type.BYTE,
            Flags.DEFAULT,
            Flags.Companion::fromByte,
            Flags.Companion::toByte
        )
    val AIR_TICKS = index(MetadataEntry.Type.VAR_INT, 300)
    val CUSTOM_NAME = index(MetadataEntry.Type.OPT_COMPONENT, null)
    val CUSTOM_NAME_VISIBLE = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_SILENT = index(MetadataEntry.Type.BOOLEAN, false)
    val HAS_NO_GRAVITY = index(MetadataEntry.Type.BOOLEAN, false)
    val POSE = index(MetadataEntry.Type.ENTITY_POSE, Pose.STANDING)
    val TICKS_FROZEN = index(MetadataEntry.Type.VAR_INT, 0)

    data class Flags(
        val isOnFire: Boolean = false,
        val isSneaking: Boolean = false, // hides the name tag, but does not make the entity visually sneak
        val isSprinting: Boolean = false, // shows sprinting particles when on ground
        val isSwimming: Boolean = false,
        val isInvisible: Boolean = false,
        val hasGlowingEffects: Boolean = false,
        val isGliding: Boolean = false,
    ) {
        companion object {
            val DEFAULT = Flags()

            fun toByte(flags: Flags): Byte {
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

            fun fromByte(byte: Byte): Flags {
                val byte = byte.toInt()
                return Flags(
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

    enum class Pose {
        STANDING,
        FALL_FLYING,
        SLEEPING,
        SWIMMING,
        SPIN_ATTACK,
        SNEAKING,
        LONG_JUMPING,
        DYING,
        CROAKING,
        USING_TONGUE,
        SITTING,
        ROARING,
        SNIFFING,
        EMERGING,
        DIGGING,
        SLIDING,
        SHOOTING,
        INHALING;
    }
}