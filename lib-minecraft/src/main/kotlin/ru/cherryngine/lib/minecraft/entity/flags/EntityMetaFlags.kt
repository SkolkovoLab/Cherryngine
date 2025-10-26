package ru.cherryngine.lib.minecraft.entity.flags

import ru.cherryngine.lib.minecraft.entity.Metadata
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec

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

        val STREAM_CODEC = StreamCodec.byteFlags(
            0x01, EntityMetaFlags::isOnFire,
            0x02, EntityMetaFlags::isSneaking,
            // 0x04 unused
            0x08, EntityMetaFlags::isSprinting,
            0x10, EntityMetaFlags::isSwimming,
            0x20, EntityMetaFlags::isInvisible,
            0x40, EntityMetaFlags::hasGlowingEffects,
            0x80, EntityMetaFlags::isGliding,
            ::EntityMetaFlags
        )

        fun metaEntry(value: EntityMetaFlags): Metadata.Entry<EntityMetaFlags> =
            Metadata.Entry(Metadata.TYPE_BYTE, value, STREAM_CODEC)
    }
}