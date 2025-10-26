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

        val toShow = mutableListOf<McEntity>()
        val toHide = mutableListOf<McEntity>()

        val chunks = ChunkUtils.getChunksInRange(player.clientChunkPos, distance).toSet()
        val entities = entitiesProvider()
        val currentVisible = player.currentVisibleEntities

        sequenceOf(entities, currentVisible).flatten().forEach { entity ->
            if (entity !in currentVisible && (entity in entities && entity.chunkPos in chunks)) {
                toShow.add(entity)
            }
            if (entity in currentVisible && (entity !in entities || entity.chunkPos !in chunks)) {
                toHide.add(entity)
            }
        }
        toShow.forEach { entity ->
            entity.show(player)
            currentVisible += entity
        }
        toHide.forEach { entity ->
            entity.hide(player)
            currentVisible -= entity
        }
    }
}