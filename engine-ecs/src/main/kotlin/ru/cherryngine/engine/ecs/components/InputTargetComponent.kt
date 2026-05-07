package ru.cherryngine.engine.ecs.components

import com.github.quillraven.fleks.ComponentType
import ru.cherryngine.engine.ecs.EcsComponent
import java.util.UUID

/**
 * Маркер на entity: вот этот клиент передаёт инпуты этой entity.
 * Несколько entity могут иметь InputTargetComponent одного игрока одновременно.
 * Каждая система сама решает какие именно инпуты ей нужны.
 */
data class InputTargetComponent(
    val playerUuid: UUID,
) : EcsComponent<InputTargetComponent> {
    override fun type() = InputTargetComponent

    companion object : ComponentType<InputTargetComponent>()
}
