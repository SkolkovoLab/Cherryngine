package ru.cherryngine.platform.minecraft.java.view

import ru.cherryngine.platform.minecraft.java.world.ChunkPos

interface StaticViewableProvider {
    fun getStaticViewables(chunkPos: ChunkPos): Set<BlocksViewable>
}