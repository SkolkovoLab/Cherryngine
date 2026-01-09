package ru.cherryngine.lib.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

sealed interface World {
    val dimensionType: DimensionType
    fun getBlock(position: Vec3I): Block?
    fun getChunkData(pos: ChunkPos): ChunkData
    fun getLightData(pos: ChunkPos): LightData?
    fun getSectionOrNull(position: SectionPos): ChunkSection?
}
