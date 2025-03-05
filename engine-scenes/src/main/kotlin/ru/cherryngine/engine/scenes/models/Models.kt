package ru.cherryngine.engine.scenes.models

import ru.cherryngine.engine.core.server.ClientConnection
import ru.cherryngine.engine.core.entity.EngineEntity
import ru.cherryngine.engine.scenes.models.api.BodyPart

sealed class Models {

    abstract fun render(viewer: ClientConnection)
    abstract fun getPart(id: String): List<EngineEntity>?

    data class SkeletonModel(
        val bodyParts: Map<BodyPart, List<EngineEntity>>
    ) : Models() {
        override fun render(viewer: ClientConnection) {
            bodyParts.values.flatten().let { entity -> entity.forEach { it.show(viewer) } }
        }

        override fun getPart(id: String): List<EngineEntity>? {
            return bodyParts[BodyPart.from(id)]
        }
    }

    data class FreeModel(
        val entities: HashMap<String, EngineEntity>
    ) : Models() {
        override fun render(viewer: ClientConnection) {
            entities.values.forEach {  entity -> entity.show(viewer) }
        }

        override fun getPart(id: String): List<EngineEntity>? {
            return entities[id]?.let { listOf(it) }
        }
    }
}
