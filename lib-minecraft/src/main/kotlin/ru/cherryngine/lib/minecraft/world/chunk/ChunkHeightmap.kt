package ru.cherryngine.lib.minecraft.world.chunk

import io.netty.buffer.ByteBuf
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import ru.cherryngine.lib.math.Vec3I
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.ChunkUtils.ceilLog2
import ru.cherryngine.lib.minecraft.utils.bitstorage.SimpleBitStorage
import ru.cherryngine.lib.minecraft.world.block.Block
import java.util.function.Predicate

class ChunkHeightmap(
    val chunk: Chunk,
    val type: Type,
) {
    private val bitStorage = SimpleBitStorage(ceilLog2(chunk.dimensionType.height + 1), 256)
    fun getRawData(): LongArray = bitStorage.data

    companion object {
        val STREAM_CODEC = object : StreamCodec<ChunkHeightmap> {
            override fun write(
                buffer: ByteBuf,
                value: ChunkHeightmap,
            ) {
                StreamCodec.LONG_ARRAY.write(buffer, value.getRawData())
            }

            override fun read(buffer: ByteBuf): ChunkHeightmap {
                TODO("Not yet implemented")
            }
        }

        fun generate(chunk: Chunk, toGenerate: Set<Type>) {
            val size = toGenerate.size
            val heightmaps = ObjectArrayList<ChunkHeightmap>(size)
            val iterator = heightmaps.iterator()
            val highest = highestSectionY(chunk) + 16
            for (x in 0 until 16) for (z in 0 until 16) {
                toGenerate.forEach { type -> heightmaps.add(chunk.getOrCreateHeightmap(type)) }
                for (y in highest - 1 downTo chunk.dimensionType.minY) {
                    val block = chunk.getBlock(Vec3I(x, y, z))
                    if (!block.isAir()) {
                        while (iterator.hasNext()) {
                            val heightmap = iterator.next()
                            if (!heightmap.type.predicate.test(block)) continue
                            heightmap.set(x, z, y + 1)
                            iterator.remove()
                        }
                        if (heightmaps.isEmpty) break
                        iterator.back(size)
                    }
                }
            }
        }

        fun highestSectionY(chunk: Chunk): Int {
            val highestSectionIndex = highestNonEmptySectionIndex(chunk) ?: return chunk.dimensionType.minY
            return getSectionYFromSectionIndex(chunk, highestSectionIndex)
        }

        fun highestNonEmptySectionIndex(chunk: Chunk): Int? {
            for (i in chunk.sections.size - 1 downTo 0) {
                val section = chunk.sections[i]
                if (!section.hasOnlyAir()) return i
            }
            return null
        }

        fun getSectionYFromSectionIndex(chunk: Chunk, index: Int): Int {
            return index + chunk.dimensionType.minY shr 4
        }
    }

    @Suppress("AddOperatorModifier")
    fun set(x: Int, z: Int, y: Int) {
        bitStorage[indexOf(x, z)] = y - chunk.dimensionType.minY
    }

    private fun indexOf(x: Int, z: Int): Int = x + z * 16

    enum class Type(val id: Int, val predicate: Predicate<Block>) {
        WORLD_SURFACE(1, { block -> !block.isAir() }),
        MOTION_BLOCKING(4, { block -> block.registryBlock.isSolid || block.registryBlock.isLiquid }),
        MOTION_BLOCKING_NO_LEAVES(5, { block -> block.registryBlock.isSolid && !block.identifier.endsWith("_leaves") });

        companion object {
            val BY_ID = entries.associateBy { it.id }
            val STREAM_CODEC = StreamCodec.VAR_INT.transform<Type>({ BY_ID[it]!! }, { it.id })
        }
    }
}