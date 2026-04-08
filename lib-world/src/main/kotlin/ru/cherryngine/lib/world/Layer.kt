package ru.cherryngine.lib.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

interface Layer {
    val id: String

    /**
     * Блок, считающийся "явным воздухом" — вырезает дыры в нижестоящих слоях.
     * null = слой только добавляет блоки, вырезать не умеет.
     * По умолчанию: STRUCTURE_VOID.
     */
    val voidMarker: Block?

    /**
     * null = "в этом слое позиция не определена, смотри нижестоящий слой"
     */
    fun getBlock(pos: Vec3I): Block?

    fun getSectionOrNull(pos: SectionPos): ChunkSection?
    fun getLightData(pos: ChunkPos): LightData?
    fun getBlockEntities(pos: ChunkPos): Map<Vec3I, BlockEntity>
    fun getHeightMaps(pos: ChunkPos): Map<ChunkHeightmapType, LongArray>
}

data class LayerEntry(val layer: Layer, val priority: Int)
