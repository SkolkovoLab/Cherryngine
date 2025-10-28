package ru.cherryngine.lib.minecraft.world.palette

import ru.cherryngine.lib.minecraft.utils.bitsToRepresent
import java.util.Arrays

object Palettes {
    fun pack(ints: IntArray, bitsPerEntry: Int): LongArray {
        val intsPerLong = Math.floor(64.0 / bitsPerEntry).toInt()
        val longs = LongArray(Math.ceil(ints.size / intsPerLong.toDouble()).toInt())
        val mask = (1L shl bitsPerEntry) - 1L
        for (i in longs.indices) {
            for (intIndex in 0 until intsPerLong) {
                val bitIndex = intIndex * bitsPerEntry
                val intActualIndex = intIndex + i * intsPerLong
                if (intActualIndex < ints.size) {
                    longs[i] = longs[i] or ((ints[intActualIndex].toLong() and mask) shl bitIndex)
                }
            }
        }
        return longs
    }

    fun unpack(out: IntArray, `in`: LongArray, bitsPerEntry: Int) {
        assert(`in`.isNotEmpty()) { "unpack input array is zero" }
        val intsPerLong = Math.floor(64.0 / bitsPerEntry).toDouble()
        val intsPerLongCeil = Math.ceil(intsPerLong).toInt()
        val mask = (1L shl bitsPerEntry) - 1L
        for (i in out.indices) {
            val longIndex = i / intsPerLongCeil
            val subIndex = i % intsPerLongCeil
            out[i] = ((`in`[longIndex].ushr(bitsPerEntry * subIndex)) and mask).toInt()
        }
    }

    fun maxPaletteSize(bitsPerEntry: Int): Int = 1 shl bitsPerEntry

    fun arrayLength(dimension: Int, bitsPerEntry: Int): Int {
        val elementCount = dimension * dimension * dimension
        val valuesPerLong = 64 / bitsPerEntry
        return (elementCount + valuesPerLong - 1) / valuesPerLong
    }

    fun read(dimension: Int, bitsPerEntry: Int, values: LongArray, x: Int, y: Int, z: Int): Int {
        val sectionIndex = sectionIndex(dimension, x, y, z)
        val valuesPerLong = 64 / bitsPerEntry
        val index = sectionIndex / valuesPerLong
        val bitIndex = (sectionIndex - index * valuesPerLong) * bitsPerEntry
        val mask = (1 shl bitsPerEntry) - 1
        return ((values[index].ushr(bitIndex)).toInt() and mask)
    }

    fun write(dimension: Int, bitsPerEntry: Int, values: LongArray, x: Int, y: Int, z: Int, value: Int): Int {
        val valuesPerLong = 64 / bitsPerEntry
        val sectionIndex = sectionIndex(dimension, x, y, z)
        val index = sectionIndex / valuesPerLong
        val bitIndex = (sectionIndex - index * valuesPerLong) * bitsPerEntry

        val block = values[index]
        val clear = (1L shl bitsPerEntry) - 1L
        val oldBlock = (block ushr bitIndex) and clear
        values[index] = (block and (clear shl bitIndex).inv()) or ((value.toLong() and clear) shl bitIndex)
        return oldBlock.toInt()
    }

    fun fill(bitsPerEntry: Int, values: LongArray, value: Int) {
        val valuesPerLong = 64 / bitsPerEntry
        var block = 0L
        for (i in 0 until valuesPerLong) block = block or ((value.toLong()) shl (i * bitsPerEntry))
        Arrays.fill(values, block)
    }

    fun count(bitsPerEntry: Int, values: LongArray): Int {
        val valuesPerLong = 64 / bitsPerEntry
        var count = 0
        val mask = (1 shl bitsPerEntry) - 1
        for (block in values) {
            for (i in 0 until valuesPerLong) {
                count += ((block ushr (i * bitsPerEntry)).toInt() and mask)
            }
        }
        return count
    }

    fun sectionIndex(dimension: Int, x: Int, y: Int, z: Int): Int {
        val dimensionBitCount = bitsToRepresent(dimension - 1)
        return (y shl (dimensionBitCount shl 1)) or (z shl dimensionBitCount) or x
    }

    // Optimized operations
    fun getAllFill(dimension: Byte, value: Int, consumer: Palette.EntryConsumer) {
        for (y in 0 until dimension) {
            for (z in 0 until dimension) {
                for (x in 0 until dimension) {
                    consumer.accept(x.toInt(), y.toInt(), z.toInt(), value)
                }
            }
        }
    }
}
