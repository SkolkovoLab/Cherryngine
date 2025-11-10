package ru.cherryngine.impl.demo.ecs.testimpl.components

import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.math.YawPitch

data class ClientPositionComponent(
    var clientPosition: Vec3D,
    var clientYawPitch: YawPitch,
) : Component<ClientPositionComponent> {
    override fun type() = ClientPositionComponent
    companion object : ComponentType<ClientPositionComponent>()
}