package ru.cherryngine.lib.minecraft.world.palette

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import ru.cherryngine.lib.minecraft.utils.bitsToRepresent
import ru.cherryngine.lib.minecraft.world.palette.internal.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator

internal class PaletteImpl(
    val dimension: Byte,
    val minBitsPerEntry: Byte,
    val maxBitsPerEntry: Byte,
    val directBits: Byte,
) : Palette {
    companion object {
        const val SECTION_BLOCK_COUNT: Int = 16 * 16 * 16
        private val WRITE_CACHE: ThreadLocal<IntArray> = ThreadLocal.withInitial { IntArray(SECTION_BLOCK_COUNT) }
    }

    var internalPalette: InternalPalette = SingleValuedPalette(0)

    constructor(
        dimension: Byte, minBitsPerEntry: Byte, maxBitsPerEntry: Byte, directBits: Byte,
        bitsPerEntry: Byte, count: Int, palette: IntArray, values: LongArray,
    ) : this(dimension, minBitsPerEntry, maxBitsPerEntry, directBits) {
        internalPalette = newInternalPalette(maxBitsPerEntry, bitsPerEntry, count, palette, values)
    }

    fun newInternalPalette(
        maxBitsPerEntry: Byte,
        bitsPerEntry: Byte, count: Int, palette: IntArray, values: LongArray,
    ): InternalPalette {
        val isSingle = bitsPerEntry == 0.toByte()
        val hasPalette = bitsPerEntry.toInt() <= maxBitsPerEntry.toInt()

        return when {
            isSingle -> {
                SingleValuedPalette(count)
            }

            hasPalette -> {
                IndirectPalette(bitsPerEntry, count, palette, values)
            }

            else -> {
                DirectPalette(bitsPerEntry, count, values)
            }
        }
    }

    fun newInternalPalette(
        dimension: Byte, maxBitsPerEntry: Byte, bitsPerEntry: Byte,
    ): InternalPalette {
        val isSingle = bitsPerEntry == 0.toByte()
        val hasPalette = bitsPerEntry.toInt() <= maxBitsPerEntry.toInt()
        val count = 0
        val values = LongArray(PaletteUtils.arrayLength(dimension.toInt(), bitsPerEntry.toInt()))

        return when {
            isSingle -> {
                SingleValuedPalette(0)
            }

            hasPalette -> {
                IndirectPalette(bitsPerEntry, count, intArrayOf(0), values)
            }

            else -> {
                DirectPalette(bitsPerEntry, count, values)
            }
        }
    }

    constructor(
        dimension: Byte, minBitsPerEntry: Byte, maxBitsPerEntry: Byte, directBits: Byte, bitsPerEntry: Byte,
    ) : this(
        dimension, minBitsPerEntry, maxBitsPerEntry, directBits, bitsPerEntry,
        0, intArrayOf(0), LongArray(PaletteUtils.arrayLength(dimension.toInt(), bitsPerEntry.toInt()))
    )

    private fun validateCoord(x: Int, y: Int, z: Int) {
        validateCoord(dimension.toInt(), x, y, z)
    }

    override fun get(x: Int, y: Int, z: Int): Int {
        validateCoord(x, y, z)
        val internalPalette = this.internalPalette
        val vals = when (internalPalette) {
            is SingleValuedPalette -> return internalPalette.value
            is IndirectPalette -> internalPalette.values
            is DirectPalette -> internalPalette.values
        }
//        val vals = internalPalette.values ?: error("values is null")
        val value = PaletteUtils.read(dimension(), internalPalette.bitsPerEntry.toInt(), vals, x, y, z)
        return paletteIndexToValue(value)
    }

    override fun getAll(consumer: Palette.EntryConsumer) {
        val internalPalette = internalPalette
        if (internalPalette is SingleValuedPalette) {
            PaletteUtils.getAllFill(dimension.toInt().toByte(), internalPalette.value, consumer)
        } else {
            retrieveAll(consumer, true)
        }
    }

    override fun getAllPresent(consumer: Palette.EntryConsumer) {
        val internalPalette = internalPalette
        if (internalPalette is SingleValuedPalette) {
            if (internalPalette.value != 0) PaletteUtils.getAllFill(
                dimension.toInt().toByte(),
                internalPalette.value,
                consumer
            )
        } else {
            retrieveAll(consumer, false)
        }
    }

    override fun set(x: Int, y: Int, z: Int, value: Int) {
        validateCoord(x, y, z)
        val palIndex = valueToPaletteIndex(value)
        val internalPalette = internalPalette as MultiValuedPalette
        val oldValue = PaletteUtils.write(
            dimension(),
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

    fun setDirect(internalPalette: MultiValuedPalette, x: Int, y: Int, z: Int, value: Int) {
        validateCoord(x, y, z)
        val palIndex = valueToPaletteIndexDirect(internalPalette, value)
        val oldValue = PaletteUtils.write(
            dimension(),
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

    override fun fill(value: Int) {
        this.internalPalette = SingleValuedPalette(value)
    }

    override fun offset(offset: Int) {
        val internalPalette = internalPalette
        if (internalPalette is SingleValuedPalette) {
            internalPalette.value += offset
        } else {
            replaceAll { x, y, z, v -> v + offset }
        }
    }

    override fun replace(oldValue: Int, newValue: Int) {
        if (oldValue == newValue) return
        val internalPalette = internalPalette
        if (internalPalette is SingleValuedPalette) {
            if (oldValue == internalPalette.value) fill(newValue)
        } else {
            if (internalPalette is IndirectPalette) {
                val map = internalPalette.valueToPaletteMap
                val index = map.get(oldValue)
                if (index == -1) return
                val countUpdate = newValue == 0 || oldValue == 0
                val cnt = if (countUpdate) count(oldValue) else -1
                if (cnt == 0) return
                internalPalette.paletteToValueList.set(index, newValue)
                map.remove(oldValue)
                map.put(newValue, index)
                if (newValue == 0) {
                    internalPalette.count -= cnt
                } else if (oldValue == 0) {
                    internalPalette.count += cnt
                }
            } else {
                replaceAll { x, y, z, v -> if (v == oldValue) newValue else v }
            }
        }
    }

    override fun setAll(supplier: Palette.EntrySupplier) {
        val cache = WRITE_CACHE.get()
        val dimension = dimension()
        var fillValue = -1
        var cnt = 0
        var index = 0
        for (y in 0 until dimension) for (z in 0 until dimension) for (x in 0 until dimension) {
            val value = supplier.get(x, y, z)
            if (fillValue != -2) {
                if (fillValue == -1) {
                    fillValue = value
                } else if (fillValue != value) {
                    fillValue = -2
                }
            }
            if (value != 0) cnt++
            cache[index++] = value
        }
        assert(index == maxSize())
        if (fillValue < 0) {
            if (internalPalette.bitsPerEntry.toInt() != directBits.toInt()) resize(directBits)
            val internalPalette = this.internalPalette as MultiValuedPalette
            updateAll(cache)
            internalPalette.count = cnt
        } else {
            fill(fillValue)
        }
    }

    override fun replace(x: Int, y: Int, z: Int, operator: IntUnaryOperator) {
        validateCoord(x, y, z)
        val oldValue = get(x, y, z)
        val newValue = operator.applyAsInt(oldValue)
        if (oldValue != newValue) set(x, y, z, newValue)
    }

    override fun replaceAll(function: Palette.EntryFunction) {
        val cache = WRITE_CACHE.get()
        val arrayIndex = AtomicInteger()
        val cnt = AtomicInteger()
        getAll { x, y, z, value ->
            val newValue = function.apply(x, y, z, value)
            val idx = arrayIndex.getPlain()
            arrayIndex.setPlain(idx + 1)
            cache[idx] = newValue
            if (newValue != 0) cnt.setPlain(cnt.getPlain() + 1)
        }
        assert(arrayIndex.getPlain() == maxSize())
        val internalPalette = this.internalPalette as MultiValuedPalette
        if (internalPalette.bitsPerEntry.toInt() != directBits.toInt()) resize(directBits)
        updateAll(cache)
        internalPalette.count = cnt.getPlain()
    }

    override fun copyFrom(source: Palette, offsetX: Int, offsetY: Int, offsetZ: Int) {
        source as PaletteImpl
        if (offsetX == 0 && offsetY == 0 && offsetZ == 0) {
            copyFrom(source)
            return
        }

        val internalPalette = this.internalPalette
        val sourceInternalPalette = source.internalPalette

        val sourceDimension = source.dimension()
        val targetDimension = this.dimension()
        require(sourceDimension == targetDimension) { "Source palette dimension ($sourceDimension) must equal target palette dimension ($targetDimension)" }

        val maxX = minOf(sourceDimension, targetDimension - offsetX)
        val maxY = minOf(sourceDimension, targetDimension - offsetY)
        val maxZ = minOf(sourceDimension, targetDimension - offsetZ)
        if (maxX <= 0 || maxY <= 0 || maxZ <= 0) return

        if (sourceInternalPalette is SingleValuedPalette) {
            if (sourceInternalPalette.value == 0) return
            val value = sourceInternalPalette.value
            val paletteValue = valueToPaletteIndex(value)
            internalPalette as MultiValuedPalette
            for (y in 0 until maxY) for (z in 0 until maxZ) for (x in 0 until maxX) {
                val targetX = offsetX + x
                val targetY = offsetY + y
                val targetZ = offsetZ + z
                val oldValue = PaletteUtils.write(
                    targetDimension,
                    internalPalette.bitsPerEntry.toInt(),
                    internalPalette.values,
                    targetX,
                    targetY,
                    targetZ,
                    paletteValue
                )
                val wasAir = oldValue == 0
                val isAir = paletteValue == 0
                if (wasAir != isAir) internalPalette.count += if (wasAir) 1 else -1
            }
            return
        }

        sourceInternalPalette as MultiValuedPalette
        internalPalette as MultiValuedPalette // хз схуяли мы так решили

        if (sourceInternalPalette.isEmpty) {
            var removedBlocks = 0
            for (y in 0 until maxY) for (z in 0 until maxZ) for (x in 0 until maxX) {
                val targetX = offsetX + x
                val targetY = offsetY + y
                val targetZ = offsetZ + z
                val oldValue =
                    PaletteUtils.write(
                        targetDimension,
                        internalPalette.bitsPerEntry.toInt(),
                        internalPalette.values,
                        targetX,
                        targetY,
                        targetZ,
                        0
                    )
                if (oldValue != 0) removedBlocks++
            }
            internalPalette.count -= removedBlocks
            return
        }

        val sourceValues = sourceInternalPalette.values
        val sourceBitsPerEntry = sourceInternalPalette.bitsPerEntry.toInt()
        val sourceMask = (1 shl sourceBitsPerEntry) - 1
        val sourceValuesPerLong = 64 / sourceBitsPerEntry
        val sourceDimensionBitCount = bitsToRepresent(sourceDimension - 1)
        val sourceShiftedDimensionBitCount = sourceDimensionBitCount shl 1
        val sourcePaletteIds =
            if (sourceInternalPalette is IndirectPalette) sourceInternalPalette.paletteToValueList.elements() else null

        var countDelta = 0
        for (y in 0 until maxY) for (z in 0 until maxZ) for (x in 0 until maxX) {
            val targetX = offsetX + x
            val targetY = offsetY + y
            val targetZ = offsetZ + z
            val sourceIndex = y shl sourceShiftedDimensionBitCount or (z shl sourceDimensionBitCount) or x
            val longIndex = sourceIndex / sourceValuesPerLong
            val bitIndex = (sourceIndex - longIndex * sourceValuesPerLong) * sourceBitsPerEntry
            val sourcePaletteIndex = (sourceValues[longIndex].ushr(bitIndex).toInt() and sourceMask)
            val sourceValue =
                if (sourcePaletteIds != null && sourcePaletteIndex < sourcePaletteIds.size) sourcePaletteIds[sourcePaletteIndex] else sourcePaletteIndex
            val targetPaletteIndex = valueToPaletteIndex(sourceValue)
            val oldValue = PaletteUtils.write(
                targetDimension,
                internalPalette.bitsPerEntry.toInt(),
                internalPalette.values,
                targetX,
                targetY,
                targetZ,
                targetPaletteIndex
            )
            val wasAir = oldValue == 0
            val isAir = targetPaletteIndex == 0
            if (wasAir != isAir) countDelta += if (wasAir) 1 else -1
        }
        internalPalette.count += countDelta
    }

    override fun copyFrom(source: Palette) {
        source as PaletteImpl

        val sourceDimension = source.dimension()
        val targetDimension = this.dimension()
        require(sourceDimension == targetDimension) { "Source palette dimension ($sourceDimension) must equal target palette dimension ($targetDimension)" }

        this.internalPalette = source.internalPalette.clone()
    }

    override fun count(): Int {
        val internalPalette = this.internalPalette
        return if (internalPalette is SingleValuedPalette) {
            if (internalPalette.value == 0) 0 else maxSize()
        } else {
            internalPalette as MultiValuedPalette
            internalPalette.count
        }
    }

    override fun count(value: Int): Int {
        val internalPalette = this.internalPalette
        if (internalPalette is SingleValuedPalette) return if (internalPalette.value == value) maxSize() else 0
        internalPalette as MultiValuedPalette
        if (value == 0) return maxSize() - count()
        var queryValue = value
        if (internalPalette is IndirectPalette) {
            val lookup = internalPalette.valueToPaletteMap.getOrDefault(value, -1)
            if (lookup == -1) return 0
            queryValue = lookup
        }
        var result = 0
        val size = maxSize()
        val bits = internalPalette.bitsPerEntry.toInt()
        val valuesPerLong = 64 / bits
        val mask = (1 shl bits) - 1
        val arr = internalPalette.values
        var idx = 0
        for (i in arr.indices) {
            var block = arr[i]
            val end = minOf(valuesPerLong, size - idx)
            repeat(end) {
                if ((block.toInt() and mask) == queryValue) result++
                block = block ushr bits
                idx++
            }
        }
        return result
    }

    override fun any(value: Int): Boolean {
        val internalPalette = this.internalPalette
        if (internalPalette is SingleValuedPalette) return internalPalette.value == value
        internalPalette as MultiValuedPalette
        if (value == 0) return maxSize() != internalPalette.count
        var queryValue = value
        if (internalPalette is IndirectPalette) {
            val lookup = internalPalette.valueToPaletteMap.getOrDefault(value, -1)
            if (lookup == -1) return false
            queryValue = lookup
        }
        val size = maxSize()
        val bits = internalPalette.bitsPerEntry.toInt()
        val valuesPerLong = 64 / bits
        val mask = (1 shl bits) - 1
        val arr = internalPalette.values
        var idx = 0
        for (i in arr.indices) {
            var block = arr[i]
            val end = minOf(valuesPerLong, size - idx)
            repeat(end) {
                if ((block.toInt() and mask) == queryValue) return true
                block = block ushr bits
                idx++
            }
        }
        return false
    }

    override fun bitsPerEntry(): Int = internalPalette.bitsPerEntry.toInt()

    override fun dimension(): Int = dimension.toInt()

    override fun optimize(focus: Palette.Optimization) {
        val bpe = internalPalette.bitsPerEntry.toInt()
        if (bpe == 0) return

        val uniqueValues: IntSet = IntOpenHashSet()
        getAll { _, _, _, v -> uniqueValues.add(v) }
        val uniqueCount = uniqueValues.size

        if (uniqueCount == 1) {
            fill(uniqueValues.iterator().nextInt())
            return
        }

        if (focus == Palette.Optimization.SPEED) {
            resize(directBits)
        } else {
            val optimalBits = bitsToRepresent(uniqueCount - 1).toByte()
            if (optimalBits < internalPalette.bitsPerEntry) {
                resize(optimalBits)
            }
        }
    }

    override fun compare(palette: Palette): Boolean {
        palette as PaletteImpl
        val dimension = this.dimension()
        if (palette.dimension() != dimension) return false
        val internalPalette = this.internalPalette
        val otherInternalPalette = palette.internalPalette
        if (otherInternalPalette.isEmpty && internalPalette.isEmpty) return true
        if (otherInternalPalette is SingleValuedPalette && internalPalette is SingleValuedPalette) {
            return otherInternalPalette.value == internalPalette.value
        }
        for (y in 0 until dimension) for (z in 0 until dimension) for (x in 0 until dimension) {
            val v1 = this[x, y, z]
            val v2 = palette[x, y, z]
            if (v1 != v2) return false
        }
        return true
    }

    @Suppress("MethodDoesntCallSuperMethod")
    override fun clone(): Palette {
        val clone = PaletteImpl(dimension, minBitsPerEntry, maxBitsPerEntry, directBits)
        clone.internalPalette = internalPalette.clone()
        return clone
    }

    private fun retrieveAll(consumer: Palette.EntryConsumer, consumeEmpty: Boolean) {
        val internalPalette = this.internalPalette as MultiValuedPalette
        if (!consumeEmpty && internalPalette.isEmpty) return
        val arr = internalPalette.values
        val dim = this.dimension()
        val bpe = internalPalette.bitsPerEntry.toInt()
        val magicMask = (1 shl bpe) - 1
        val valuesPerLong = 64 / bpe
        val size = maxSize()
        val dimMinus = dim - 1
        val ids = if (internalPalette is IndirectPalette) internalPalette.paletteToValueList.elements() else null
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
                    val result = if (ids != null && paletteIndex < ids.size) ids[paletteIndex] else paletteIndex
                    consumer.accept(x, y, z, result)
                }
            }
        }
    }

    private fun updateAll(paletteValues: IntArray) {
        val internalPalette = this.internalPalette as MultiValuedPalette
        val size = maxSize()
        assert(paletteValues.size >= size)
        val bpe = internalPalette.bitsPerEntry.toInt()
        val valuesPerLong = 64 / bpe
        val clear = (1L shl bpe) - 1L
        val arr = internalPalette.values
        for (i in arr.indices) {
            var block = arr[i]
            val startIndex = i * valuesPerLong
            val endIndex = minOf(startIndex + valuesPerLong, size)
            for (index in startIndex until endIndex) {
                val bitIndex = (index - startIndex) * bpe
                block = block and (clear shl bitIndex).inv() or ((paletteValues[index].toLong() and clear) shl bitIndex)
            }
            arr[i] = block
        }
    }

    fun resize(newBitsPerEntry: Byte) {
        var newBpe = newBitsPerEntry
        if (newBpe.toInt() > maxBitsPerEntry.toInt()) newBpe = directBits
        val internalPalette = this.internalPalette
        val newInternalPalette = newInternalPalette(dimension, maxBitsPerEntry, newBitsPerEntry) as MultiValuedPalette

        if (internalPalette is IndirectPalette && newInternalPalette is IndirectPalette) {
            newInternalPalette.paletteToValueList = internalPalette.paletteToValueList
            newInternalPalette.valueToPaletteMap = internalPalette.valueToPaletteMap
        }
        getAll { x, y, z, value -> setDirect(newInternalPalette, x, y, z, value) }

        this.internalPalette = newInternalPalette
    }

    override fun paletteIndexToValue(value: Int): Int {
        val internalPalette = this.internalPalette
        return if (internalPalette is IndirectPalette) internalPalette.paletteToValueList.elements()[value] else value
    }

    override fun valueToPaletteIndex(value: Int): Int {
        if (internalPalette is DirectPalette) return value
        if (internalPalette is SingleValuedPalette) resize(minBitsPerEntry)
        val internalPalette = this.internalPalette as IndirectPalette
        val lastPaletteIndex = internalPalette.paletteToValueList.size
        val bpe = internalPalette.bitsPerEntry.toInt()
        if (lastPaletteIndex >= PaletteUtils.maxPaletteSize(bpe)) {
            resize((bpe + 1).toByte())
            return valueToPaletteIndex(value)
        }
        val map = internalPalette.valueToPaletteMap
        val lookup = map.putIfAbsent(value, lastPaletteIndex)
        if (lookup != -1) return lookup
        internalPalette.paletteToValueList.add(value)
        return lastPaletteIndex
    }

    fun valueToPaletteIndexDirect(internalPalette: MultiValuedPalette, value: Int): Int {
        if (internalPalette is DirectPalette) return value
        internalPalette as IndirectPalette
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

    override fun singleValue(): Int {
        val internalPalette = this.internalPalette
        return if (internalPalette is SingleValuedPalette) internalPalette.value else -1
    }

    override fun indexedValues(): LongArray? {
        val internalPalette = this.internalPalette
        return if (internalPalette is MultiValuedPalette) internalPalette.values else null
    }

    private fun validateCoord(dimension: Int, x: Int, y: Int, z: Int) {
        if (x < 0 || y < 0 || z < 0)
            throw IllegalArgumentException("Coordinates must be non-negative")
        if (x >= dimension || y >= dimension || z >= dimension)
            throw IllegalArgumentException("Coordinates must be less than the dimension size, got $x, $y, $z for dimension $dimension")
    }

    private fun validateDimension(dimension: Int) {
        if (dimension <= 1 || (dimension and dimension - 1) != 0)
            throw IllegalArgumentException("Dimension must be a positive power of 2, got $dimension")
    }
}