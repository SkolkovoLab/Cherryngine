package ru.cherryngine.lib.minecraft.world.palette

import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import it.unimi.dsi.fastutil.ints.IntSet
import ru.cherryngine.lib.minecraft.utils.bitsToRepresent
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.IntUnaryOperator

internal class PaletteImpl(
    var internalPalette: InternalPalette,
) : Palette {
    companion object {
        const val SECTION_BLOCK_COUNT: Int = 16 * 16 * 16
        private val WRITE_CACHE: ThreadLocal<IntArray> = ThreadLocal.withInitial { IntArray(SECTION_BLOCK_COUNT) }
    }

    val dimension: Byte get() = internalPalette.dimension
    val minBitsPerEntry: Byte get() = internalPalette.minBitsPerEntry
    val maxBitsPerEntry: Byte get() = internalPalette.maxBitsPerEntry
    val directBits: Byte get() = internalPalette.directBits

    private fun validateCoord(x: Int, y: Int, z: Int) {
        InternalPaletteUtils.validateCoord(dimension.toInt(), x, y, z)
    }

    override fun get(x: Int, y: Int, z: Int): Int {
        validateCoord(x, y, z)
        return internalPalette.get(x, y, z)
    }

    override fun getAll(consumer: Palette.EntryConsumer) {
        internalPalette.getAll(consumer)
    }

    override fun getAllPresent(consumer: Palette.EntryConsumer) {
        internalPalette.getAllPresent(consumer)
    }

    override fun set(x: Int, y: Int, z: Int, value: Int) {
        validateCoord(x, y, z)
        val palIndex = valueToPaletteIndex(value)
        val internalPalette = internalPalette as InternalPalette.MultiValued
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
        this.internalPalette =
            InternalPalette.SingleValued(dimension, minBitsPerEntry, maxBitsPerEntry, directBits, value)
    }

    override fun offset(offset: Int) {
        val internalPalette = internalPalette
        if (internalPalette is InternalPalette.SingleValued) {
            internalPalette.value += offset
        } else {
            replaceAll { _, _, _, v -> v + offset }
        }
    }

    override fun replace(oldValue: Int, newValue: Int) {
        if (oldValue == newValue) return
        val internalPalette = internalPalette
        if (internalPalette is InternalPalette.SingleValued) {
            if (oldValue == internalPalette.value) fill(newValue)
        } else {
            if (internalPalette is InternalPalette.Indirect) {
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
                replaceAll { _, _, _, v -> if (v == oldValue) newValue else v }
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
            val internalPalette = this.internalPalette as InternalPalette.MultiValued
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
            arrayIndex.plain = idx + 1
            cache[idx] = newValue
            if (newValue != 0) cnt.plain = cnt.getPlain() + 1
        }
        assert(arrayIndex.getPlain() == maxSize())
        val internalPalette = this.internalPalette as InternalPalette.MultiValued
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

        if (sourceInternalPalette is InternalPalette.SingleValued) {
            if (sourceInternalPalette.value == 0) return
            val value = sourceInternalPalette.value
            val paletteValue = valueToPaletteIndex(value)
            internalPalette as InternalPalette.MultiValued
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

        sourceInternalPalette as InternalPalette.MultiValued
        internalPalette as InternalPalette.MultiValued // хз схуяли мы так решили

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
            if (sourceInternalPalette is InternalPalette.Indirect) sourceInternalPalette.paletteToValueList.elements() else null

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
        return if (internalPalette is InternalPalette.SingleValued) {
            if (internalPalette.value == 0) 0 else maxSize()
        } else {
            internalPalette as InternalPalette.MultiValued
            internalPalette.count
        }
    }

    override fun count(value: Int): Int {
        val internalPalette = this.internalPalette
        if (internalPalette is InternalPalette.SingleValued) return if (internalPalette.value == value) maxSize() else 0
        internalPalette as InternalPalette.MultiValued
        if (value == 0) return maxSize() - count()
        var queryValue = value
        if (internalPalette is InternalPalette.Indirect) {
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
        if (internalPalette is InternalPalette.SingleValued) return internalPalette.value == value
        internalPalette as InternalPalette.MultiValued
        if (value == 0) return maxSize() != internalPalette.count
        var queryValue = value
        if (internalPalette is InternalPalette.Indirect) {
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
        if (otherInternalPalette is InternalPalette.SingleValued && internalPalette is InternalPalette.SingleValued) {
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
        return PaletteImpl(internalPalette.clone())
    }

    private fun updateAll(paletteValues: IntArray) {
        val internalPalette = this.internalPalette as InternalPalette.MultiValued
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
        internalPalette = InternalPaletteUtils.resize(internalPalette, newBitsPerEntry)
    }

    override fun paletteIndexToValue(value: Int): Int {
        val internalPalette = this.internalPalette
        return if (internalPalette is InternalPalette.Indirect) internalPalette.paletteToValueList.elements()[value] else value
    }

    override fun valueToPaletteIndex(value: Int): Int {
        if (internalPalette is InternalPalette.Direct) return value
        if (internalPalette is InternalPalette.SingleValued) resize(minBitsPerEntry)
        val internalPalette = this.internalPalette as InternalPalette.Indirect
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

    override fun singleValue(): Int {
        val internalPalette = this.internalPalette
        return if (internalPalette is InternalPalette.SingleValued) internalPalette.value else -1
    }

    override fun indexedValues(): LongArray? {
        val internalPalette = this.internalPalette
        return if (internalPalette is InternalPalette.MultiValued) internalPalette.values else null
    }
}