@file:Suppress("unused")

package ru.cherryngine.engine.core.world

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.kyori.adventure.nbt.CompoundBinaryTag
import net.minestom.server.coordinate.CoordConversion
import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.palette.Palette
import net.minestom.server.network.NetworkBuffer
import net.minestom.server.network.packet.server.play.ChunkDataPacket
import net.minestom.server.network.packet.server.play.data.ChunkData
import net.minestom.server.network.packet.server.play.data.LightData
import net.minestom.server.world.DimensionType
import ru.cherryngine.engine.core.world.api.Chunk
import ru.cherryngine.engine.core.world.api.ChunkSupplier
import java.util.*


open class BlockHolder(
    dimensionType: DimensionType,
    val chunkSupplier: ChunkSupplier,
) : Block.Getter, Block.Setter {
    val minSection = dimensionType.minY() / Chunk.CHUNK_SECTION_SIZE
    val maxSection = (dimensionType.minY() + dimensionType.height()) / Chunk.CHUNK_SECTION_SIZE
    val sectionCount = maxSection - minSection
    private val chunks: MutableMap<Long, Chunk?> = Long2ObjectOpenHashMap()

    private fun getChunk(chunkX: Int, chunkZ: Int): Chunk? {
        return chunks.computeIfAbsent(CoordConversion.chunkIndex(chunkX, chunkZ)) {
            chunkSupplier.create(this, chunkX, chunkZ)
        }
    }

    fun generatePacket(chunkX: Int, chunkZ: Int): ChunkDataPacket? {
        val chunk = getChunk(chunkX, chunkZ) ?: return null
        val data = NetworkBuffer.makeArray { networkBuffer ->
            for (section: Section in chunk.sections) {
                networkBuffer.write(NetworkBuffer.SHORT, section.blockPalette().count().toShort())
                networkBuffer.write(Palette.BLOCK_SERIALIZER, section.blockPalette())
                networkBuffer.write(Palette.BIOME_SERIALIZER, section.biomePalette())
            }
        }
        return ChunkDataPacket(
            chunkX, chunkZ,
            ChunkData(CompoundBinaryTag.empty(), data, mapOf()),
            createLightData(chunk)
        )
    }

    private fun createLightData(chunk: Chunk): LightData {
        val skyMask = BitSet()
        val blockMask = BitSet()
        val emptySkyMask = BitSet()
        val emptyBlockMask = BitSet()
        val skyLights: MutableList<ByteArray> = ArrayList()
        val blockLights: MutableList<ByteArray> = ArrayList()

        var index = 0
        for (section in chunk.sections) {
            index++
            val skyLight = section.skyLight().array()
            val blockLight = section.blockLight().array()
            if (skyLight.isNotEmpty()) {
                skyLights.add(skyLight)
                skyMask.set(index)
            } else {
                emptySkyMask.set(index)
            }
            if (blockLight.isNotEmpty()) {
                blockLights.add(blockLight)
                blockMask.set(index)
            } else {
                emptyBlockMask.set(index)
            }
        }
        return LightData(
            skyMask, blockMask,
            emptySkyMask, emptyBlockMask,
            skyLights, blockLights
        )
    }

    override fun setBlock(x: Int, y: Int, z: Int, block: Block) {
        val chunk = getChunk(x shr 4, z shr 4) ?: return
        val section = chunk.sections[(y shr 4) - minSection]
        section.blockPalette()[x and 0xF, y and 0xF, z and 0xF] = block.stateId().toInt()
    }

    override fun getBlock(x: Int, y: Int, z: Int, condition: Block.Getter.Condition): Block {
        return getBlockNullable(x, y, z, condition) ?: Block.AIR
    }

    private fun getBlockNullable(x: Int, y: Int, z: Int, condition: Block.Getter.Condition): Block? {
        val chunk = getChunk(x shr 4, z shr 4) ?: return null
        val section = chunk.sections.getOrNull((y shr 4) - minSection) ?: return null
        val stateId = section.blockPalette()[x and 0xF, y and 0xF, z and 0xF]
        return Block.fromStateId(stateId)
    }
}
