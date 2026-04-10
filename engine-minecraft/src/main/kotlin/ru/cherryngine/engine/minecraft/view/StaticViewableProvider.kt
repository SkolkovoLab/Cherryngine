package ru.cherryngine.engine.minecraft.view

import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos

interface StaticViewableProvider {
    fun getStaticViewables(chunkPos: ChunkPos): Set<BlocksViewable>
}