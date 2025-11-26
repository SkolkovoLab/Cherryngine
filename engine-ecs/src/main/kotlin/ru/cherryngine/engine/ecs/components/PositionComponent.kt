package ru.cherryngine.engine.ecs.components

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.ecs.EcsComponent
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

data class PositionComponent(
    var position: Vec3D = Vec3D.ZERO,
    var yawPitch: YawPitch = YawPitch.ZERO,
) : EcsComponent<PositionComponent> {
    override fun type() = PositionComponent

    companion object : ComponentType<PositionComponent>()
}