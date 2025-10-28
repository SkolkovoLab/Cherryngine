package ru.cherryngine.lib.minecraft.world.palette

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import ru.cherryngine.lib.minecraft.utils.bitsToRepresent
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator

internal class PaletteImpl(
    val dimensionByte: Byte,
    val minBitsPerEntryByte: Byte,
    val maxBitsPerEntryByte: Byte,
    val directBitsByte: Byte,
) : Palette {

    companion object {
        val SECTION_BLOCK_COUNT: Int = 16 * 16 * 16
        private val WRITE_CACHE: ThreadLocal<IntArray> = ThreadLocal.withInitial { IntArray(SECTION_BLOCK_COUNT) }
    }

    private val dimension: Byte = dimensionByte
    private val minBitsPerEntry: Byte = minBitsPerEntryByte
    private val maxBitsPerEntry: Byte = maxBitsPerEntryByte
    val directBits: Byte = directBitsByte

    var bitsPerEntry: Byte = 0
    var count: Int = 0 // Serve as the single value if bitsPerEntry == 0

    var values: LongArray? = null
    // palette index = value
    var paletteToValueList: IntArrayList? = null
    // value = palette index
    private var valueToPaletteMap: Int2IntOpenHashMap? = null

    constructor(
        dimension: Byte, minBitsPerEntry: Byte, maxBitsPerEntry: Byte, directBits: Byte,
        bitsPerEntry: Byte, count: Int, palette: IntArray, values: LongArray,
    ) : this(dimension, minBitsPerEntry, maxBitsPerEntry, directBits) {
        this.bitsPerEntry = bitsPerEntry
        this.count = count
        this.values = values
        if (hasPalette()) {
            this.paletteToValueList = IntArrayList(palette)
            this.valueToPaletteMap = Int2IntOpenHashMap(palette.size)
            this.valueToPaletteMap!!.defaultReturnValue(-1)
            for (i in palette.indices) {
                this.valueToPaletteMap!!.put(palette[i], i)
            }
        }
    }

    constructor(
        dimension: Byte, minBitsPerEntry: Byte, maxBitsPerEntry: Byte, directBits: Byte, bitsPerEntry: Byte,
    ) : this(dimension, minBitsPerEntry, maxBitsPerEntry, directBits, bitsPerEntry,
        0, intArrayOf(0), LongArray(Palettes.arrayLength(dimension.toInt(), bitsPerEntry.toInt()))
    )

    private fun validateCoord(x: Int, y: Int, z: Int) {
        validateCoord(dimension.toInt(), x, y, z)
    }

    override fun get(x: Int, y: Int, z: Int): Int {
        validateCoord(x, y, z)
        if (bitsPerEntry.toInt() == 0) return count
        val vals = values ?: error("values is null")
        val value = Palettes.read(dimension(), bitsPerEntry.toInt(), vals, x, y, z)
        return paletteIndexToValue(value)
    }

    override fun getAll(consumer: Palette.EntryConsumer) {
        if (bitsPerEntry.toInt() == 0) {
            Palettes.getAllFill(dimension.toInt().toByte(), count, consumer)
        } else {
            retrieveAll(consumer, true)
        }
    }

    override fun getAllPresent(consumer: Palette.EntryConsumer) {
        if (bitsPerEntry.toInt() == 0) {
            if (count != 0) Palettes.getAllFill(dimension.toInt().toByte(), count, consumer)
        } else {
            retrieveAll(consumer, false)
        }
    }

    override fun set(x: Int, y: Int, z: Int, value: Int) {
        validateCoord(x, y, z)
        val palIndex = valueToPaletteIndex(value)
        val oldValue = Palettes.write(dimension(), bitsPerEntry.toInt(), values!!, x, y, z, palIndex)
        val currentAir = oldValue == 0
        if (currentAir != (palIndex == 0)) this.count += if (currentAir) 1 else -1
    }

    override fun fill(value: Int) {
        this.bitsPerEntry = 0
        this.count = value
        this.values = null
        this.paletteToValueList = null
        this.valueToPaletteMap = null
    }

    override fun offset(offset: Int) {
        if (offset == 0) return
        if (bitsPerEntry.toInt() == 0) {
            this.count += offset
        } else {
            replaceAll { x, y, z, v -> v + offset }
        }
    }

    override fun replace(oldValue: Int, newValue: Int) {
        if (oldValue == newValue) return
        if (bitsPerEntry.toInt() == 0) {
            if (oldValue == count) fill(newValue)
        } else {
            if (hasPalette()) {
                val map = valueToPaletteMap ?: return
                val index = map.get(oldValue)
                if (index == -1) return
                val countUpdate = newValue == 0 || oldValue == 0
                val cnt = if (countUpdate) count(oldValue) else -1
                if (cnt == 0) return
                paletteToValueList!!.set(index, newValue)
                map.remove(oldValue)
                map.put(newValue, index)
                if (newValue == 0) {
                    this.count -= cnt
                } else if (oldValue == 0) {
                    this.count += cnt
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
        for (y in 0 until dimension) {
            for (z in 0 until dimension) {
                for (x in 0 until dimension) {
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
            }
        }
        assert(index == maxSize())
        if (fillValue < 0) {
            if (bitsPerEntry.toInt() != directBits.toInt()) resize(directBits)
            updateAll(cache)
            this.count = cnt
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
        if (bitsPerEntry.toInt() != directBits.toInt()) resize(directBits)
        updateAll(cache)
        this.count = cnt.getPlain()
    }

    override fun copyFrom(source: Palette, offsetX: Int, offsetY: Int, offsetZ: Int) {
        if (offsetX == 0 && offsetY == 0 && offsetZ == 0) {
            copyFrom(source)
            return
        }

        val sourcePalette = source as PaletteImpl
        val sourceDimension = sourcePalette.dimension()
        val targetDimension = this.dimension()
        if (sourceDimension != targetDimension) {
            throw IllegalArgumentException("Source palette dimension ($sourceDimension) must equal target palette dimension ($targetDimension)")
        }

        val maxX = minOf(sourceDimension, targetDimension - offsetX)
        val maxY = minOf(sourceDimension, targetDimension - offsetY)
        val maxZ = minOf(sourceDimension, targetDimension - offsetZ)
        if (maxX <= 0 || maxY <= 0 || maxZ <= 0) return

        if (sourcePalette.bitsPerEntry.toInt() == 0) {
            if (sourcePalette.count == 0) return
            val value = sourcePalette.count
            val paletteValue = valueToPaletteIndex(value)
            for (y in 0 until maxY) {
                val targetY = offsetY + y
                for (z in 0 until maxZ) {
                    val targetZ = offsetZ + z
                    for (x in 0 until maxX) {
                        val targetX = offsetX + x
                        val oldValue = Palettes.write(targetDimension, bitsPerEntry.toInt(), values!!, targetX, targetY, targetZ, paletteValue)
                        val wasAir = oldValue == 0
                        val isAir = paletteValue == 0
                        if (wasAir != isAir) this.count += if (wasAir) 1 else -1
                    }
                }
            }
            return
        }

        if (sourcePalette.count == 0) {
            var removedBlocks = 0
            for (y in 0 until maxY) {
                val targetY = offsetY + y
                for (z in 0 until maxZ) {
                    val targetZ = offsetZ + z
                    for (x in 0 until maxX) {
                        val targetX = offsetX + x
                        val oldValue = Palettes.write(targetDimension, bitsPerEntry.toInt(), values!!, targetX, targetY, targetZ, 0)
                        if (oldValue != 0) removedBlocks++
                    }
                }
            }
            this.count -= removedBlocks
            return
        }

        val sourceValues = sourcePalette.values ?: LongArray(0)
        val sourceBitsPerEntry = sourcePalette.bitsPerEntry.toInt()
        val sourceMask = (1 shl sourceBitsPerEntry) - 1
        val sourceValuesPerLong = 64 / sourceBitsPerEntry
        val sourceDimensionBitCount = bitsToRepresent(sourceDimension - 1)
        val sourceShiftedDimensionBitCount = sourceDimensionBitCount shl 1
        val sourcePaletteIds = if (sourcePalette.hasPalette()) sourcePalette.paletteToValueList!!.elements() else null

        var countDelta = 0
        for (y in 0 until maxY) {
            val targetY = offsetY + y
            for (z in 0 until maxZ) {
                val targetZ = offsetZ + z
                for (x in 0 until maxX) {
                    val targetX = offsetX + x
                    val sourceIndex = y shl sourceShiftedDimensionBitCount or (z shl sourceDimensionBitCount) or x
                    val longIndex = sourceIndex / sourceValuesPerLong
                    val bitIndex = (sourceIndex - longIndex * sourceValuesPerLong) * sourceBitsPerEntry
                    val sourcePaletteIndex = (sourceValues[longIndex].ushr(bitIndex).toInt() and sourceMask)
                    val sourceValue = if (sourcePaletteIds != null && sourcePaletteIndex < sourcePaletteIds.size) sourcePaletteIds[sourcePaletteIndex] else sourcePaletteIndex
                    val targetPaletteIndex = valueToPaletteIndex(sourceValue)
                    val oldValue = Palettes.write(targetDimension, bitsPerEntry.toInt(), values!!, targetX, targetY, targetZ, targetPaletteIndex)
                    val wasAir = oldValue == 0
                    val isAir = targetPaletteIndex == 0
                    if (wasAir != isAir) countDelta += if (wasAir) 1 else -1
                }
            }
        }
        this.count += countDelta
    }

    override fun copyFrom(source: Palette) {
        val sourcePalette = source as PaletteImpl
        val sourceDimension = sourcePalette.dimension()
        val targetDimension = this.dimension()
        if (sourceDimension != targetDimension) throw IllegalArgumentException("Source palette dimension ($sourceDimension) must equal target palette dimension ($targetDimension)")
        if (sourcePalette.bitsPerEntry.toInt() == 0) {
            fill(sourcePalette.count)
            return
        }
        if (sourcePalette.count == 0) {
            fill(0)
            return
        }

        this.bitsPerEntry = sourcePalette.bitsPerEntry
        this.count = sourcePalette.count
        this.values = sourcePalette.values?.clone()
        this.paletteToValueList = sourcePalette.paletteToValueList?.let { IntArrayList(it) }
        this.valueToPaletteMap = sourcePalette.valueToPaletteMap?.let { Int2IntOpenHashMap(it) }
    }

    override fun count(): Int {
        return if (bitsPerEntry.toInt() == 0) {
            if (count == 0) 0 else maxSize()
        } else {
            count
        }
    }

    override fun count(value: Int): Int {
        if (bitsPerEntry.toInt() == 0) return if (count == value) maxSize() else 0
        if (value == 0) return maxSize() - count()
        var queryValue = value
        if (hasPalette()) {
            val lookup = valueToPaletteMap!!.getOrDefault(value, -1)
            if (lookup == -1) return 0
            queryValue = lookup
        }
        var result = 0
        val size = maxSize()
        val bits = bitsPerEntry.toInt()
        val valuesPerLong = 64 / bits
        val mask = (1 shl bits) - 1
        val arr = values!!
        var idx = 0
        for (i in arr.indices) {
            var block = arr[i]
            val end = minOf(valuesPerLong, size - idx)
            for (j in 0 until end) {
                if ((block.toInt() and mask) == queryValue) result++
                block = block ushr bits
                idx++
            }
        }
        return result
    }

    override fun any(value: Int): Boolean {
        if (bitsPerEntry.toInt() == 0) return count == value
        if (value == 0) return maxSize() != count
        var queryValue = value
        if (hasPalette()) {
            val lookup = valueToPaletteMap!!.getOrDefault(value, -1)
            if (lookup == -1) return false
            queryValue = lookup
        }
        val size = maxSize()
        val bits = bitsPerEntry.toInt()
        val valuesPerLong = 64 / bits
        val mask = (1 shl bits) - 1
        val arr = values!!
        var idx = 0
        for (i in arr.indices) {
            var block = arr[i]
            val end = minOf(valuesPerLong, size - idx)
            for (j in 0 until end) {
                if ((block.toInt() and mask) == queryValue) return true
                block = block ushr bits
                idx++
            }
        }
        return false
    }

    override fun bitsPerEntry(): Int = bitsPerEntry.toInt()

    override fun dimension(): Int = dimension.toInt()

    override fun optimize(focus: Palette.Optimization) {
        val bpe = this.bitsPerEntry.toInt()
        if (bpe == 0) return

        val uniqueValues: IntSet = IntOpenHashSet()
        getAll { x, y, z, v -> uniqueValues.add(v) }
        val uniqueCount = uniqueValues.size

        if (uniqueCount == 1) {
            fill(uniqueValues.iterator().nextInt())
            return
        }

        if (focus == Palette.Optimization.SPEED) {
            resize(directBits)
        } else {
            val optimalBits = bitsToRepresent(uniqueCount - 1).toByte()
            if (optimalBits < bitsPerEntry) {
                resize(optimalBits)
            }
        }
    }

    override fun compare(p: Palette): Boolean {
        val palette = p as PaletteImpl
        val dimension = this.dimension()
        if (palette.dimension() != dimension) return false
        if (palette.count == 0 && this.count == 0) return true
        if (palette.bitsPerEntry.toInt() == 0 && this.bitsPerEntry.toInt() == 0 && palette.count == this.count) return true
        for (y in 0 until dimension) {
            for (z in 0 until dimension) {
                for (x in 0 until dimension) {
                    val v1 = this.get(x, y, z)
                    val v2 = palette.get(x, y, z)
                    if (v1 != v2) return false
                }
            }
        }
        return true
    }

    @Suppress("MethodDoesntCallSuperMethod")
    override public fun clone(): Palette {
        val clone = PaletteImpl(dimension, minBitsPerEntry, maxBitsPerEntry, directBits)
        clone.bitsPerEntry = this.bitsPerEntry
        clone.count = this.count
        if (bitsPerEntry.toInt() == 0) return clone
        clone.values = this.values?.clone()
        clone.paletteToValueList = this.paletteToValueList?.clone()
        clone.valueToPaletteMap = this.valueToPaletteMap?.clone()
        return clone
    }

    private fun retrieveAll(consumer: Palette.EntryConsumer, consumeEmpty: Boolean) {
        if (!consumeEmpty && count == 0) return
        val arr = this.values!!
        val dim = this.dimension()
        val bpe = this.bitsPerEntry.toInt()
        val magicMask = (1 shl bpe) - 1
        val valuesPerLong = 64 / bpe
        val size = maxSize()
        val dimMinus = dim - 1
        val ids = if (hasPalette()) paletteToValueList!!.elements() else null
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
        val size = maxSize()
        assert(paletteValues.size >= size)
        val bpe = this.bitsPerEntry.toInt()
        val valuesPerLong = 64 / bpe
        val clear = (1L shl bpe) - 1L
        val arr = this.values!!
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
        val palette = PaletteImpl(dimension, minBitsPerEntry, maxBitsPerEntry, directBits, newBpe)
        if (paletteToValueList != null) palette.paletteToValueList = paletteToValueList
        if (valueToPaletteMap != null) palette.valueToPaletteMap = valueToPaletteMap
        getAll { x, y, z, v -> palette.set(x, y, z, v) }
        this.bitsPerEntry = palette.bitsPerEntry
        this.values = palette.values
        this.paletteToValueList = palette.paletteToValueList
        this.valueToPaletteMap = palette.valueToPaletteMap
        assert(values != null)
    }

    override fun paletteIndexToValue(value: Int): Int {
        return if (hasPalette()) paletteToValueList!!.elements()[value] else value
    }

    override fun valueToPaletteIndex(value: Int): Int {
        if (!hasPalette()) return value
        if (values == null) resize(minBitsPerEntry)
        val lastPaletteIndex = this.paletteToValueList!!.size
        val bpe = this.bitsPerEntry.toInt()
        if (lastPaletteIndex >= Palettes.maxPaletteSize(bpe)) {
            resize((bpe + 1).toByte())
            return valueToPaletteIndex(value)
        }
        val map = this.valueToPaletteMap!!
        val lookup = map.putIfAbsent(value, lastPaletteIndex)
        if (lookup != -1) return lookup
        this.paletteToValueList!!.add(value)
        return lastPaletteIndex
    }

    override fun singleValue(): Int = if (bitsPerEntry.toInt() == 0) count else -1

    override fun indexedValues(): LongArray? = values

    fun hasPalette(): Boolean = bitsPerEntry.toInt() <= maxBitsPerEntry.toInt()

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