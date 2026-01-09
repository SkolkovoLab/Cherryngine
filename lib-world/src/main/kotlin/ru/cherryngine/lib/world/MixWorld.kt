package ru.cherryngine.lib.world

import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.network.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.network.protocol.types.SectionPos
import ru.cherryngine.lib.minecraft.registry.Registries
import ru.cherryngine.lib.minecraft.registry.keys.Blocks
import ru.cherryngine.lib.minecraft.registry.types.DimensionType
import ru.cherryngine.lib.minecraft.world.block.Block
import ru.cherryngine.lib.minecraft.world.chunk.ChunkData
import ru.cherryngine.lib.minecraft.world.chunk.ChunkSection
import ru.cherryngine.lib.minecraft.world.light.LightData

class MixWorld(
    override val dimensionType: DimensionType
) : World {
    val layers = mutableListOf<World>()

    override fun getBlock(position: Vec3I): Block? {
        layers.asReversed().forEach {
            val block = it.getBlock(position)
            if (block == null || block.isAir()) return@forEach
            if (block.registryBlock.id == Registries.block[Blocks.STRUCTURE_VOID].id) return Block.AIR
        }

        return Block.AIR
    }

    override fun getSectionOrNull(position: SectionPos): ChunkSection? {
        // TODO заменить каст на разворачивание слоёного мира
        val chunkSections = layers.mapNotNull { (it as BaseWorld).getSectionOrNull(position) }
        if (chunkSections.isEmpty()) return null
        if (chunkSections.size == 1) return chunkSections[0]
        return MixTools.mixSection(chunkSections)
    }

    val minSection = dimensionType.minY / 16

    override fun getChunkData(pos: ChunkPos): ChunkData {
        val sections = List(dimensionType.height / 16) {
            val sectionPos = SectionPos(pos.x, it + minSection, pos.z)
            getSectionOrNull(sectionPos) ?: ChunkSection.EMPTY
        }

        return ChunkData(
            mapOf(), // TODO
            sections,
            mapOf() // TODO
        )
    }

    override fun getLightData(pos: ChunkPos): LightData? {
        return layers.firstOrNull()?.getLightData(pos)
    }
}