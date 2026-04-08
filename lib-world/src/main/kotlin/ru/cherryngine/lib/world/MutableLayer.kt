package ru.cherryngine.lib.world

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.block.BlockEntity
import ru.cherryngine.lib.minecraft.world.chunk.ChunkHeightmapType
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

/**
 * Разреженный изменяемый слой.
 *
 * Семантика getBlock:
 *   - секция отсутствует → null ("не определено")
 *   - stateId == 0 внутри секции → null ("не определено, наследуй из нижестоящего")
 *   - stateId != 0 → Block (может быть voidMarker = "явный воздух")
 *
 * Операции:
 *   setBlock(pos, block) — block.getStateId() должен быть != 0
 *   putVoid(pos)         — явный воздух, вырезает дыру в нижестоящих слоях
 *   remove(pos)          — убрать определение, наследовать снизу
 */
class MutableLayer(
    override val id: String,
    override val voidMarker: Block? = Block.STRUCTURE_VOID,
) : Layer {
    private val sectionsMap: MutableMap<Long, ChunkSection> = Long2ObjectOpenHashMap()

    override fun getBlock(pos: Vec3I): Block? {
        val section = sectionsMap[SectionPos.fromBlockPos(pos).pack()] ?: return null
        val stateId = section.getBlock(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
        )
        return if (stateId == 0) null else Block.getBlockByStateId(stateId)
    }

    fun setBlock(pos: Vec3I, block: Block) {
        require(block.getStateId() != 0) { "Используй putVoid() для явного воздуха" }
        val section = sectionsMap.computeIfAbsent(SectionPos.fromBlockPos(pos).pack()) { ChunkSection.empty() }
        section.setBlock(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
            block.getStateId(),
        )
    }

    fun putVoid(pos: Vec3I) {
        val marker = checkNotNull(voidMarker) { "voidMarker = null, слой не может вырезать дыры" }
        setBlock(pos, marker)
    }

    fun remove(pos: Vec3I) {
        val section = sectionsMap[SectionPos.fromBlockPos(pos).pack()] ?: return
        section.setBlock(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
            0,
        )
    }

    override fun getSectionOrNull(pos: SectionPos): ChunkSection? = sectionsMap[pos.pack()]
    override fun getLightData(pos: ChunkPos): LightData? = null
    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, BlockEntity> = emptyMap()
    override fun getHeightMaps(pos: ChunkPos): Map<ChunkHeightmapType, LongArray> = emptyMap()
}
