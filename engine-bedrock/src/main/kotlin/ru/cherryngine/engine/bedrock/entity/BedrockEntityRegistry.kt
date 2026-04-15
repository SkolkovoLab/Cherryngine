package ru.cherryngine.engine.bedrock.entity

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
