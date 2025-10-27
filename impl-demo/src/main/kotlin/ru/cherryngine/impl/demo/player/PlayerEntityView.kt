package ru.cherryngine.impl.demo.player

import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class PlayerEntityView(
    val player: Player,
    var entitiesProvider: () -> Set<McEntity>,
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 1
    }

    fun init() {
        update()
    }

    fun update() {
        val distance = DEFAULT_RENDER_DISTANCE

        val chunks = ChunkUtils.getChunksInRange(player.clientChunkPos, distance).toSet()
        val entities: Set<McEntity> = entitiesProvider()
        val currentVisible: MutableSet<McEntity> = player.currentVisibleEntities

        currentVisible.removeIf { entity ->
            val shouldHide = entity !in entities || entity.chunkPos !in chunks
            if (shouldHide) entity.hide(player)
            shouldHide
        }

        entities.forEach { entity ->
            val shouldShow = entity !in currentVisible && entity.chunkPos in chunks
            if (shouldShow) {
                entity.show(player)
                currentVisible.add(entity)
            }
        }
    }
}