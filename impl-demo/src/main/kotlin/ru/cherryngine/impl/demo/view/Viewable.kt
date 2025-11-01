package ru.cherryngine.impl.demo.view

import ru.cherryngine.impl.demo.player.Player
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: Player)
    fun hide(player: Player)
    val viewerPredicate: (Player) -> Boolean
}