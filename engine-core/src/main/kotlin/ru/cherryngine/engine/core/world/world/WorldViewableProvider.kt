package ru.cherryngine.engine.core.world.world

import ru.cherryngine.engine.core.view.BlocksViewable
import ru.cherryngine.engine.core.view.StaticViewableProvider
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.world.World

interface WorldViewableProvider : StaticViewableProvider {
    val world: World

    val chunkViewables: Map<ChunkPos, BlocksViewable>
}

