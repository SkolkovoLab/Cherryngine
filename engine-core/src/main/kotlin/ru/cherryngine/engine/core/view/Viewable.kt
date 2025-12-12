package ru.cherryngine.engine.core.view

import ru.cherryngine.engine.core.player.Player
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: Player)
    fun hide(player: Player)
    val viewerPredicate: (Player) -> Boolean
}