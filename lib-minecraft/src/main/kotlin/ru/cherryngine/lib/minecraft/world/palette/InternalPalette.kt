package ru.cherryngine.lib.minecraft.world.palette

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList

internal sealed interface InternalPalette {
    val dimension: Byte
    val minBitsPerEntry: Byte
    val maxBitsPerEntry: Byte
    val directBits: Byte

    val bitsPerEntry: Byte
    val isEmpty: Boolean

    fun clone(): InternalPalette
    fun get(x: Int, y: Int, z: Int): Int
    fun retrieveAll(consumer: Palette.EntryConsumer, consumeEmpty: Boolean)
    fun getAll(consumer: Palette.EntryConsumer) = retrieveAll(consumer, true)
    fun getAllPresent(consumer: Palette.EntryConsumer) = retrieveAll(consumer, false)

    class SingleValued(
        override val dimension: Byte,
        override val minBitsPerEntry: Byte,
        override val maxBitsPerEntry: Byte,
        override val directBits: Byte,

        var value: Int,
    ) : InternalPalette {
        override val bitsPerEntry: Byte get() = 0
        override val isEmpty: Boolean get() = value == 0
        override fun clone(): InternalPalette {
            return SingleValued(
                dimension,
                minBitsPerEntry,
                maxBitsPerEntry,
                directBits,
                value
            )
        }

        override fun get(x: Int, y: Int, z: Int): Int {
            return value
        }

        override fun retrieveAll(consumer: Palette.EntryConsumer, consumeEmpty: Boolean) {
            if (!consumeEmpty && value == 0) return
            PaletteUtils.getAllFill(dimension, value, consumer)
        }
    }

    sealed interface MultiValued : InternalPalette {
        var count: Int
        var values: LongArray
    }

    class Indirect(
        override val dimension: Byte,
        override val minBitsPerEntry: Byte,
        override val maxBitsPerEntry: Byte,
        override val directBits: Byte,

        bitsPerEntry: Byte,
        count: Int,
        palette: IntArray,
        values: LongArray,
    ) : MultiValued {
        override var bitsPerEntry: Byte = 0
        override var count: Int = 0
        override val isEmpty: Boolean get() = count == 0

        override var values: LongArray

        // palette index = value
        var paletteToValueList: IntArrayList

        // value = palette index
        var valueToPaletteMap: Int2IntOpenHashMap

        init {
            this.bitsPerEntry = bitsPerEntry
            this.count = count
            this.values = values
            this.paletteToValueList = IntArrayList(palette)
            this.valueToPaletteMap = Int2IntOpenHashMap(palette.size).apply {
                defaultReturnValue(-1)
                palette.forEachIndexed { i, value -> put(value, i) }
            }
        }

        override fun clone(): InternalPalette {
            val newInternalPalette = Indirect(
                dimension,
                minBitsPerEntry,
                maxBitsPerEntry,
                directBits,
                bitsPerEntry,
                count,
                IntArray(0),
                values.clone()
            )
            newInternalPalette.paletteToValueList = IntArrayList(paletteToValueList)
            newInternalPalette.valueToPaletteMap = Int2IntOpenHashMap(valueToPaletteMap)
            return newInternalPalette
        }

        override fun get(x: Int, y: Int, z: Int): Int {
            val value = PaletteUtils.read(dimension.toInt(), bitsPerEntry.toInt(), values, x, y, z)
            return paletteIndexToValue(value)
        }

        fun paletteIndexToValue(value: Int): Int {
            return paletteToValueList.elements()[value]
        }

        override fun retrieveAll(consumer: Palette.EntryConsumer, consumeEmpty: Boolean) {
            InternalPaletteUtils.retrieveAll(this, consumer, consumeEmpty) { paletteToValueList.getInt(it) }
        }
    }

    class Direct(
        override val dimension: Byte,
        override val minBitsPerEntry: Byte,
        override val maxBitsPerEntry: Byte,
        override val directBits: Byte,

        bitsPerEntry: Byte,
        count: Int,
        values: LongArray,
    ) : MultiValued {
        override var bitsPerEntry: Byte = 0
        override var count: Int = 0
        override val isEmpty: Boolean get() = count == 0

        override var values: LongArray

        init {
            this.bitsPerEntry = bitsPerEntry
            this.count = count
            this.values = values
        }

        override fun clone(): InternalPalette {
            return Direct(
                dimension,
                minBitsPerEntry,
                maxBitsPerEntry,
                directBits,
                bitsPerEntry,
                count,
                values.clone()
            )
        }

        override fun get(x: Int, y: Int, z: Int): Int {
            return PaletteUtils.read(dimension.toInt(), bitsPerEntry.toInt(), values, x, y, z)
        }

        override fun retrieveAll(consumer: Palette.EntryConsumer, consumeEmpty: Boolean) {
            InternalPaletteUtils.retrieveAll(this, consumer, consumeEmpty) { it }
        }
    }
}
