package ru.cherryngine.platform.minecraft.java.world

import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.heightmap.Heightmap
import net.minestom.server.network.packet.server.play.data.LightData
import ru.cherryngine.lib.math.Vec3I

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

    fun getSectionOrNull(pos: SectionPos): Section?
    fun getLightData(pos: ChunkPos): LightData?
    fun getBlockEntities(pos: ChunkPos): Map<Vec3I, Block>
    fun getHeightMaps(pos: ChunkPos): Map<Heightmap.Type, LongArray>
}

data class LayerEntry(val layer: Layer, val priority: Int)
