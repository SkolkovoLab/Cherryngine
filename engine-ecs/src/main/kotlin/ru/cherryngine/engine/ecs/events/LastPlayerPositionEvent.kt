package ru.cherryngine.engine.ecs.events

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

data class LastPlayerPositionEvent(
    val position: Vec3D = Vec3D.ZERO,
    val yawPitch: YawPitch = YawPitch.ZERO,
) : EcsEvent<LastPlayerPositionEvent> {
    override fun type() = LastPlayerPositionEvent

    companion object : ComponentType<LastPlayerPositionEvent>()
}