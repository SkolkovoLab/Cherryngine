package ru.cherryngine.impl.demo.view

import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.server.Connection

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: Connection)
    fun hide(player: Connection)
    val viewerPredicate: (Connection) -> Boolean
}