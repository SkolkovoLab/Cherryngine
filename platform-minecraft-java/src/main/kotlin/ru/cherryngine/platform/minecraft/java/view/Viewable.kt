package ru.cherryngine.platform.minecraft.java.view

import ru.cherryngine.platform.minecraft.java.player.MinecraftPlayer
import ru.cherryngine.platform.minecraft.java.world.ChunkPos

interface Viewable {
    val chunkPos: ChunkPos
    fun show(player: MinecraftPlayer)
    fun hide(player: MinecraftPlayer)
    val viewerPredicate: (MinecraftPlayer) -> Boolean
}