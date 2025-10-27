package ru.cherryngine.lib.minecraft.entity

@Suppress("PropertyName")
sealed class BoatMeta : AbstractVehicleMeta() {
    companion object : BoatMeta()

    val IS_LEFT_PADDLE_TURNING = index(MetadataEntry.Type.BOOLEAN, false)
    val IS_RIGHT_PADDLE_TURNING = index(MetadataEntry.Type.BOOLEAN, false)
    val SPLASH_TIMER = index(MetadataEntry.Type.VAR_INT, 0)
}