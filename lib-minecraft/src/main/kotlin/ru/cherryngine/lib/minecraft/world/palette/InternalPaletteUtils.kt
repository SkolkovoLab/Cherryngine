package ru.cherryngine.lib.minecraft.world.palette

import ru.cherryngine.lib.minecraft.utils.bitsToRepresent

internal object InternalPaletteUtils {

    fun newInternalPalette(
        dimension: Byte,
        minBitsPerEntry: Byte,
        maxBitsPerEntry: Byte,
        directBits: Byte,
        bitsPerEntry: Byte,
    ): InternalPalette {
        if (bitsPerEntry == 0.toByte()) {
            return InternalPalette.SingleValued(
                dimension,
                minBitsPerEntry,
                maxBitsPerEntry,
                directBits,
                0
            )
        }

        val hasPalette = bitsPerEntry.toInt() <= maxBitsPerEntry.toInt()
        val values = LongArray(PaletteUtils.arrayLength(dimension.toInt(), bitsPerEntry.toInt()))
        val count = 0

        return if (hasPalette) {
            InternalPalette.Indirect(
                dimension,
                minBitsPerEntry,
                maxBitsPerEntry,
                directBits,
                bitsPerEntry,
                count,
                intArrayOf(0),
                values
            )
        } else {
            InternalPalette.Direct(
                dimension,
                minBitsPerEntry,
                maxBitsPerEntry,
                directBits,
                bitsPerEntry,
                count,
                values
            )
        }
    }

    fun resize(internalPalette: InternalPalette, newBitsPerEntry: Byte): InternalPalette {
        var newBpe = newBitsPerEntry
        if (newBpe.toInt() > internalPalette.maxBitsPerEntry.toInt()) newBpe = internalPalette.directBits

        val newInternalPalette = newInternalPalette(
            internalPalette.dimension,
            internalPalette.minBitsPerEntry,
            internalPalette.maxBitsPerEntry,
            internalPalette.directBits,
            newBpe
        )
        if (newInternalPalette is InternalPalette.SingleValued) {
            newInternalPalette.value = TODO()
            return newInternalPalette
        }
        newInternalPalette as InternalPalette.MultiValued

        if (internalPalette is InternalPalette.Indirect && newInternalPalette is InternalPalette.Indirect) {
            newInternalPalette.paletteToValueList = internalPalette.paletteToValueList
            newInternalPalette.valueToPaletteMap = internalPalette.valueToPaletteMap
        }
        internalPalette.getAll { x, y, z, value -> setDirect(newInternalPalette, x, y, z, value) }

        return newInternalPalette
    }

    fun validateCoord(dimension: Int, x: Int, y: Int, z: Int) {
        if (x < 0 || y < 0 || z < 0)
            throw IllegalArgumentException("Coordinates must be non-negative")
        if (x >= dimension || y >= dimension || z >= dimension)
            throw IllegalArgumentException("Coordinates must be less than the dimension size, got $x, $y, $z for dimension $dimension")
    }

    fun setDirect(internalPalette: InternalPalette.MultiValued, x: Int, y: Int, z: Int, value: Int) {
        val dimension = internalPalette.dimension.toInt()
        validateCoord(dimension, x, y, z)
        val palIndex = valueToPaletteIndexDirect(internalPalette, value)
        val oldValue = PaletteUtils.write(
            dimension,
            internalPalette.bitsPerEntry.toInt(),
            internalPalette.values,
            x,
            y,
            z,
            palIndex
        )
        val currentAir = oldValue == 0
        if (currentAir != (palIndex == 0)) internalPalette.count += if (currentAir) 1 else -1
    }

    fun valueToPaletteIndexDirect(internalPalette: InternalPalette.MultiValued, value: Int): Int {
        if (internalPalette is InternalPalette.Direct) return value
        internalPalette as InternalPalette.Indirect
        val lastPaletteIndex = internalPalette.paletteToValueList.size
        val bpe = internalPalette.bitsPerEntry.toInt()
        if (lastPaletteIndex >= PaletteUtils.maxPaletteSize(bpe)) {
            throw IllegalStateException()
        }
        val map = internalPalette.valueToPaletteMap
        val lookup = map.putIfAbsent(value, lastPaletteIndex)
        if (lookup != -1) return lookup
        internalPalette.paletteToValueList.add(value)
        return lastPaletteIndex
    }

    fun retrieveAll(
        internalPalette: InternalPalette.MultiValued,
        consumer: Palette.EntryConsumer,
        consumeEmpty: Boolean,
        indexMapper: (Int) -> Int,
    ) {
        if (!consumeEmpty && internalPalette.isEmpty) return
        val arr = internalPalette.values
        val dim = internalPalette.dimension.toInt()
        val bpe = internalPalette.bitsPerEntry.toInt()
        val magicMask = (1 shl bpe) - 1
        val valuesPerLong = 64 / bpe
        val size = dim * dim * dim
        val dimMinus = dim - 1
        val dimensionBitCount = bitsToRepresent(dimMinus)
        val shiftedDimensionBitCount = dimensionBitCount shl 1
        for (i in arr.indices) {
            val value = arr[i]
            val startIndex = i * valuesPerLong
            val endIndex = minOf(startIndex + valuesPerLong, size)
            for (index in startIndex until endIndex) {
                val bitIndex = (index - startIndex) * bpe
                val paletteIndex = ((value ushr bitIndex).toInt() and magicMask)
                if (consumeEmpty || paletteIndex != 0) {
                    val y = index shr shiftedDimensionBitCount
                    val z = (index shr dimensionBitCount) and dimMinus
                    val x = index and dimMinus
                    val result = indexMapper(paletteIndex)
                    consumer.accept(x, y, z, result)
                }
            }
        }
    }
}