package ru.cherryngine.engine.minecraft.view

import ru.cherryngine.lib.minecraft.world.ChunkPos

interface StaticViewableProvider {
    fun getStaticViewables(chunkPos: ChunkPos): Set<BlocksViewable>
}