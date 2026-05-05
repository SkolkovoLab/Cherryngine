package ru.cherryngine.engine.core.shape

import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.Vec3I

data class RayInfo(
    val inlet: Vec3D,
    val inletDistance: Double,
    val inletNormal: Vec3D?,
    val outlet: Vec3D,
    val outletDistance: Double,
    val outletNormal: Vec3D?,
) {
    val distance = outletDistance - inletDistance
}

interface RayCastRequest {
    // return true = стоп, false = продолжать
    fun onShapeEnter(rayInfo: RayInfo, shape: ResolvedShape): Boolean = false
    fun onShapeExit(rayInfo: RayInfo, shape: ResolvedShape): Boolean = false
    fun onFinish(reason: FinishReason) {}

    enum class FinishReason {
        STOPPED_ON_ENTER,
        STOPPED_ON_EXIT,
        MAX_DISTANCE,
    }
}

// Расширение для блочного мира
interface BlockRayCastRequest : RayCastRequest {
    fun onBlockEnter(rayInfo: RayInfo, blockPos: Vec3I, material: String): Boolean = false
    fun onBlockExit(rayInfo: RayInfo, blockPos: Vec3I, material: String): Boolean = false
}
