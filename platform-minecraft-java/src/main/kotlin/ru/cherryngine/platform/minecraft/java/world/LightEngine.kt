package ru.cherryngine.platform.minecraft.java.world

import net.minestom.server.instance.Section
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.server.play.data.LightData
import ru.cherryngine.lib.math.Vec3I
import java.util.*

class LightEngine(
    val sections: List<Section>,
) {
    companion object {
        const val ARRAY_SIZE = 2048
    }

    lateinit var recalcArray: ByteArray

    private val skyLight: Array<ByteArray> = Array(sections.size) { ByteArray(0) }
    private val blockLight: Array<ByteArray> = Array(sections.size) { ByteArray(0) }

    fun createLightData(): LightData {
        val skyMask = BitSet()
        val blockMask = BitSet()
        val emptySkyMask = BitSet()
        val emptyBlockMask = BitSet()
        val skyLight = mutableListOf<ByteArray>()
        val blockLight = mutableListOf<ByteArray>()

        emptySkyMask.set(0)
        emptyBlockMask.set(0)

        emptySkyMask.set(this.skyLight.size + 1)
        emptyBlockMask.set(this.skyLight.size + 1)

        this.skyLight.indices.forEach { i ->
            if (this.hasNonZeroData(this.skyLight[i])) {
                skyMask.set(i + 1)
                skyLight.add(this.skyLight[i])
            } else {
                emptySkyMask.set(i + 1)
            }

            if (this.hasNonZeroData(this.blockLight[i])) {
                blockMask.set(i + 1)
                blockLight.add(this.blockLight[i])
            } else {
                emptyBlockMask.set(i + 1)
            }
        }

        return LightData(skyMask, blockMask, emptySkyMask, emptyBlockMask, skyLight, blockLight)
    }

    fun recalculateChunk() {
        sections.forEachIndexed { i, section ->
            recalculateSection(section, i)
        }
    }

    fun recalculateSection(section: Section, sectionIndex: Int) {
        recalcArray = ByteArray(ARRAY_SIZE)

        for (x in 0..15) for (z in 0..15) {
            var foundSolid = false
            for (y in 15 downTo 0) {
                var light = 15

                foundSolid = foundSolid || section.blockPalette().get(x, y, z) != 0

                if (foundSolid) {
                    light = 0
                }

                set(x, y, z, light)
            }
        }
        skyLight[sectionIndex] = recalcArray
        recalculateBlockLight(section, sectionIndex)
    }

    fun recalculateBlockLight(section: Section, sectionIndex: Int) {
        recalcArray = ByteArray(ARRAY_SIZE)
        val lightQueue: Queue<Vec3I> = ArrayDeque()

        for (x in 0..15) for (z in 0..15) {
            for (y in 15 downTo 0) {
                var light = 0

                val blockId = section.blockPalette().get(x, y, z)
                val block = Block.fromStateId(blockId) ?: Block.AIR
                val lightEmission = block.registry()!!.lightEmission()

                if (lightEmission > 0) {
                    light = lightEmission
                    lightQueue.add(Vec3I(x, y, z))
                }

                set(x, y, z, light)
            }
        }

        lightPropagation(lightQueue, section, sectionIndex)
        blockLight[sectionIndex] = recalcArray
    }

    fun lightPropagation(queue: Queue<Vec3I>, section: Section, sectionIndex: Int) {
        val directions = arrayOf(
            Vec3I(1, 0, 0), Vec3I(-1, 0, 0),
            Vec3I(0, 1, 0), Vec3I(0, -1, 0),
            Vec3I(0, 0, 1), Vec3I(0, 0, -1)
        )

        while (queue.isNotEmpty()) {
            val (x, y, z) = queue.poll()
            val currentLightLevel = get(x, y, z)

            if (currentLightLevel <= 1) {
                continue
            }

            val newLightLevel = currentLightLevel - 1

            for ((dX, dY, dZ) in directions) {
                val neighborX = x + dX
                val neighborY = y + dY
                val neighborZ = z + dZ

                if (neighborX in 0..15 && neighborY in 0..15 && neighborZ in 0..15) {
                    val neighborBlockId = section.blockPalette().get(neighborX, neighborY, neighborZ)
                    val neighborBlock = Block.fromStateId(neighborBlockId) ?: Block.AIR

                    if (!neighborBlock.registry()!!.occludes()) {
                        val neighborCurrentLight = get(neighborX, neighborY, neighborZ)

                        if (newLightLevel > neighborCurrentLight) {
                            set(neighborX, neighborY, neighborZ, newLightLevel)
                            queue.add(Vec3I(neighborX, neighborY, neighborZ))
                        }
                    }
                } else {
                    propagateToNeighborChunk(neighborX, neighborY, neighborZ, newLightLevel, sectionIndex)
                }
            }
        }
    }

    private fun propagateToNeighborChunk(x: Int, y: Int, z: Int, lightLevel: Int, sectionIndex: Int) {
        // неподдерживаемое соседнее распространение света; оставлено как TODO
    }

    operator fun set(x: Int, y: Int, z: Int, value: Int) {
        this[x or (z shl 4) or (y shl 8)] = value
    }

    operator fun set(index: Int, value: Int) {
        val shift = index and 1 shl 2
        val i = index ushr 1
        recalcArray[i] = (recalcArray[i].toInt() and (0xF0 ushr shift) or (value shl shift)).toByte()
    }

    operator fun get(x: Int, y: Int, z: Int): Int {
        return this[x or (z shl 4) or (y shl 8)]
    }

    operator fun get(index: Int): Int {
        val shift = index and 1 shl 2
        val i = index ushr 1

        return (recalcArray[i].toInt() ushr shift) and 0xF
    }

    fun hasNonZeroData(array: ByteArray?): Boolean {
        if (array == null) return false
        return array.isNotEmpty() && array.any { it != 0.toByte() }
    }
}
