package ru.cherryngine.impl.demo.view

import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils

class PlayerViewSystem(
    val player: Player,
) {
    val viewableProviders: MutableSet<ViewableProvider> = mutableSetOf()
    val staticViewableProviders: MutableSet<StaticViewableProvider> = mutableSetOf()

    companion object {
        const val DEFAULT_RENDER_DISTANCE = 2
    }

    fun getStaticViewables(chunkPos: ChunkPos): Set<StaticViewable> {
        return staticViewableProviders.flatMap { it.getStaticViewables(chunkPos) }.toSet()
    }

    fun getViewables(): Set<Viewable> {
        return viewableProviders.flatMap { it.viewables }.toSet()
    }

    fun update() {
        val distance = DEFAULT_RENDER_DISTANCE

        val chunks = ChunkUtils.getChunksInRange(player.clientChunkPos, distance).toSet()
        val viewables: Set<Viewable> = getViewables()
        val currentVisible: MutableSet<Viewable> = player.currentVisibleViewables
        val currentVisibleStatic: MutableSet<StaticViewable> = player.currentVisibleStaticViewables

        currentVisibleStatic.removeIf { staticViewable ->
            val staticViewables = getStaticViewables(staticViewable.chunkPos)
            val shouldHide =
                staticViewable !in staticViewables || staticViewable.chunkPos !in chunks || !staticViewable.viewerPredicate(
                    player
                )
            if (shouldHide) staticViewable.hide(player)
            shouldHide
        }

        currentVisible.removeIf { viewable ->
            val shouldHide = viewable !in viewables || viewable.chunkPos !in chunks || !viewable.viewerPredicate(player)
            if (shouldHide) viewable.hide(player)
            shouldHide
        }

        chunks.forEach { chunk ->
            val staticViewables = getStaticViewables(chunk)
            staticViewables.forEach { staticViewable ->
                val shouldShow = staticViewable !in currentVisibleStatic && staticViewable.viewerPredicate(player)
                if (shouldShow) {
                    staticViewable.show(player)
                    currentVisibleStatic.add(staticViewable)
                }
            }
        }

        viewables.forEach { viewable ->
            val shouldShow =
                viewable !in currentVisible && viewable.chunkPos in chunks && viewable.viewerPredicate(player)
            if (shouldShow) {
                viewable.show(player)
                currentVisible.add(viewable)
            }
        }
    }
}