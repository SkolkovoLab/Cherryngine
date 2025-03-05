@file:Suppress("unused")

package ru.cherryngine.engine.scenes.models.builder

import ru.cherryngine.engine.core.entity.EngineEntity
import ru.cherryngine.engine.scenes.models.Models
import ru.cherryngine.engine.scenes.models.api.BodyPart

class SkeletonModelBuilder {
    private val bodyParts = mutableMapOf<BodyPart, List<EngineEntity>>()

    fun part(type: BodyPart, entities: List<EngineEntity>) {
        bodyParts[type] = entities
    }

    fun build(): Models.SkeletonModel = Models.SkeletonModel(bodyParts)
}
