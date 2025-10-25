package ru.cherryngine.impl.demo.world.polar

import kotlin.math.ceil
import kotlin.math.floor

internal object PaletteUtil {
    fun bitsToRepresent(n: Int): Int {
        require(n > 0) { "n must be greater than 0" }
        return Integer.SIZE - Integer.numberOfLeadingZeros(n)
    }

    fun pack(ints: IntArray, bitsPerEntry: Int): LongArray {
        val intsPerLong = floor(64.0 / bitsPerEntry).toInt()
        val longs = LongArray(ceil(ints.size / intsPerLong.toDouble()).toInt())

        val mask = (1L shl bitsPerEntry) - 1L
        for (i in longs.indices) {
            for (intIndex in 0..<intsPerLong) {
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
        require(`in`.isNotEmpty()) { "unpack input array is zero" }

        val intsPerLong = floor(64.0 / bitsPerEntry)
        val intsPerLongCeil = ceil(intsPerLong).toInt()

        val mask = (1L shl bitsPerEntry) - 1L
        for (i in out.indices) {
            val longIndex = i / intsPerLongCeil
            val subIndex = i % intsPerLongCeil

            out[i] = ((`in`[longIndex] ushr (bitsPerEntry * subIndex)) and mask).toInt()
        }
    }
}
