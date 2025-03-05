@file:Suppress("unused")

package ru.cherryngine.engine.scenes.models.builder

import ru.cherryngine.engine.core.entity.EngineEntity
import ru.cherryngine.engine.scenes.models.Models

class FreeModelBuilder {
    private val entities = hashMapOf<String, EngineEntity>()

    fun entity(id: String, init: EngineEntity): FreeModelBuilder {
        entities[id] = init
        return this
    }

    fun build(): Models.FreeModel = Models.FreeModel(entities)
}
