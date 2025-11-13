package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

data class PositionComponent(
    var position: Vec3D = Vec3D.ZERO,
    var yawPitch: YawPitch = YawPitch.ZERO,
) : Component<PositionComponent> {
    override fun type() = PositionComponent

    companion object : ComponentType<PositionComponent>()
}