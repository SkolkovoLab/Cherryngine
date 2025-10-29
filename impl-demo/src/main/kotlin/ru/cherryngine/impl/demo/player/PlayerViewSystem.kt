package ru.cherryngine.impl.demo.player

import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class PlayerViewSystem(
    val player: Player,
    var viewableProvider: () -> Set<Viewable>,
) {
    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    fun init() {
        update()
    }

    fun update() {
        val distance = DEFAULT_RENDER_DISTANCE

        val chunks = ChunkUtils.getChunksInRange(player.clientChunkPos, distance).toSet()
        val viewables: Set<Viewable> = viewableProvider()
        val currentVisible: MutableSet<Viewable> = player.currentVisibleEntities

        currentVisible.removeIf { viewable ->
            val shouldHide = viewable !in viewables || viewable.chunkPos !in chunks || !viewable.viewerPredicate(player)
            if (shouldHide) viewable.hide(player)
            shouldHide
        }

        viewables.forEach { viewable ->
            val shouldShow = viewable !in currentVisible && viewable.chunkPos in chunks && viewable.viewerPredicate(player)
            if (shouldShow) {
                viewable.show(player)
                currentVisible.add(viewable)
            }
        }
    }
}