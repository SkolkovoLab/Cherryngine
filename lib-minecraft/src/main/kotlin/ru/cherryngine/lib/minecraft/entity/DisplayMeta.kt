package ru.cherryngine.lib.minecraft.entity

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.rotation.QRot

@Suppress("PropertyName")
sealed class DisplayMeta : EntityMeta() {
    companion object : DisplayMeta()

    val INTERPOLATION_DELAY = index(MetadataEntry.Type.VAR_INT, 0)
    val TRANSFORMATION_INTERPOLATION_DURATION = index(MetadataEntry.Type.VAR_INT, 0)
    val POSITION_ROTATION_INTERPOLATION_DURATION = index(MetadataEntry.Type.VAR_INT, 0)
    val TRANSLATION = index(MetadataEntry.Type.VECTOR3, Vec3D.ZERO)
    val SCALE = index(MetadataEntry.Type.VECTOR3, Vec3D.ONE)
    val ROTATION_LEFT = index(MetadataEntry.Type.QUATERNION, QRot.IDENTITY)
    val ROTATION_RIGHT = index(MetadataEntry.Type.QUATERNION, QRot.IDENTITY)
    val BILLBOARD_CONSTRAINTS = index<Byte, BillboardConstraints>(
        MetadataEntry.Type.BYTE,
        BillboardConstraints.FIXED,
        ::fromIndex,
        ::byteIndex
    )
    val BRIGHTNESS_OVERRIDE = index(MetadataEntry.Type.VAR_INT, -1)
    val VIEW_RANGE = index(MetadataEntry.Type.FLOAT, 1f)
    val SHADOW_RADIUS = index(MetadataEntry.Type.FLOAT, 0f)
    val SHADOW_STRENGTH = index(MetadataEntry.Type.FLOAT, 1f)
    val WIDTH = index(MetadataEntry.Type.FLOAT, 0f)
    val HEIGHT = index(MetadataEntry.Type.FLOAT, 0f)
    val GLOW_COLOR_OVERRIDE = index(MetadataEntry.Type.VAR_INT, -1)

    enum class BillboardConstraints {
        FIXED,
        VERTICAL,
        HORIZONTAL,
        CENTER,
    }
}