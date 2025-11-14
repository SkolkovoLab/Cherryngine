package ru.cherryngine.engine.core.view

import ru.cherryngine.engine.core.Player
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: Player)
    fun hide(player: Player)
    val viewerPredicate: (Player) -> Boolean
}