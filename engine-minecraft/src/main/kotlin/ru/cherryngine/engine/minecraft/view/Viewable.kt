package ru.cherryngine.engine.minecraft.view

import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: MinecraftPlayer)
    fun hide(player: MinecraftPlayer)
    val viewerPredicate: (MinecraftPlayer) -> Boolean
}