package ru.cherryngine.engine.minecraft.view

import ru.cherryngine.engine.minecraft.player.Player
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: Player)
    fun hide(player: Player)
    val viewerPredicate: (Player) -> Boolean
}