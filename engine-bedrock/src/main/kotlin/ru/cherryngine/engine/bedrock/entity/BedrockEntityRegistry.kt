package ru.cherryngine.engine.bedrock.entity

import ru.cherryngine.engine.core.instance.InstanceSingleton

@InstanceSingleton(platform = "bedrock")
class BedrockEntityRegistry {
    private val entities = mutableMapOf<Long, BedrockEntity>()

    fun add(entity: BedrockEntity) {
        entities[entity.runtimeEntityId] = entity
    }

    fun remove(entity: BedrockEntity) {
        entities.remove(entity.runtimeEntityId)
    }

    fun allEntities(): Collection<BedrockEntity> = entities.values
}
