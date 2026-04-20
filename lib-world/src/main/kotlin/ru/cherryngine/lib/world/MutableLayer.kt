package ru.cherryngine.lib.world

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.network.packet.server.play.data.LightData
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.utils.ChunkUtils
import ru.cherryngine.lib.minecraft.world.ChunkPos
import ru.cherryngine.lib.minecraft.world.SectionPos

/**
 * Разреженный изменяемый слой.
 *
 * Семантика getBlock:
 *   - секция отсутствует → null ("не определено")
 *   - stateId == 0 внутри секции → null ("не определено, наследуй из нижестоящего")
 *   - stateId != 0 → Block (может быть voidMarker = "явный воздух")
 */
class MutableLayer(
    override val id: String,
    override val voidMarker: Block? = Block.STRUCTURE_VOID,
    val changeTracker: MutableLayerChangeTracker? = null,
) : Layer {
    private val sectionsMap: MutableMap<Long, Section> = Long2ObjectOpenHashMap()

    override fun getBlock(pos: Vec3I): Block? {
        val section = sectionsMap[SectionPos.fromBlockPos(pos).pack()] ?: return null
        val stateId = section.blockPalette().get(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
        )
        return if (stateId == 0) null else (Block.fromStateId(stateId) ?: Block.AIR)
    }

    fun setBlock(pos: Vec3I, block: Block) {
        require(block.stateId() != 0) { "Используй putVoid() для явного воздуха" }
        val section = sectionsMap.computeIfAbsent(SectionPos.fromBlockPos(pos).pack()) { Section() }
        section.blockPalette().set(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
            block.stateId(),
        )
        changeTracker?.markDirty(id, ChunkPos(pos.x shr 4, pos.z shr 4))
    }

    fun putVoid(pos: Vec3I) {
        val marker = checkNotNull(voidMarker) { "voidMarker = null, слой не может вырезать дыры" }
        setBlock(pos, marker)
    }

    fun remove(pos: Vec3I) {
        val section = sectionsMap[SectionPos.fromBlockPos(pos).pack()] ?: return
        section.blockPalette().set(
            ChunkUtils.globalToSectionRelative(pos.x),
            ChunkUtils.globalToSectionRelative(pos.y),
            ChunkUtils.globalToSectionRelative(pos.z),
            0,
        )
        changeTracker?.markDirty(id, ChunkPos(pos.x shr 4, pos.z shr 4))
    }

    fun putSection(pos: SectionPos, section: Section) {
        sectionsMap[pos.pack()] = section
    }

    fun iterateSections(): Iterable<Map.Entry<Long, Section>> = sectionsMap.entries

    fun clear() {
        sectionsMap.clear()
    }

    override fun getSectionOrNull(pos: SectionPos): Section? = sectionsMap[pos.pack()]
    override fun getLightData(pos: ChunkPos): LightData? = null
    override fun getBlockEntities(pos: ChunkPos): Map<Vec3I, Block> = emptyMap()
    override fun getHeightMaps(pos: ChunkPos): Map<Heightmap.Type, LongArray> = emptyMap()
}
