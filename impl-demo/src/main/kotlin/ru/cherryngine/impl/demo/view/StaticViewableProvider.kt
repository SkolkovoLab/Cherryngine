package ru.cherryngine.impl.demo.view

import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos

interface StaticViewableProvider {
    fun getStaticViewables(chunkPos: ChunkPos): Set<StaticViewable>
}