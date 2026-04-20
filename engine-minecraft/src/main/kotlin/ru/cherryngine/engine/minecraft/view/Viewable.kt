package ru.cherryngine.engine.minecraft.view

import ru.cherryngine.engine.minecraft.player.MinecraftPlayer
import ru.cherryngine.lib.minecraft.world.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: MinecraftPlayer)
    fun hide(player: MinecraftPlayer)
    val viewerPredicate: (MinecraftPlayer) -> Boolean
}