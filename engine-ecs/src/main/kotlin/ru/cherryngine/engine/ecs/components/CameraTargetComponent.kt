package ru.cherryngine.engine.ecs.components

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.ecs.EcsComponent
import java.util.UUID

/**
 * Маркер на entity: камера этого игрока следует за этой entity в режиме [mode].
 */
data class CameraTargetComponent(
    val playerUuid: UUID,
    val mode: CameraMode = CameraMode.ThirdPerson(),
) : EcsComponent<CameraTargetComponent> {
    override fun type() = CameraTargetComponent

    companion object : ComponentType<CameraTargetComponent>()
}

sealed class CameraMode {
    data class ThirdPerson(val radius: Double = 6.0) : CameraMode()
}
