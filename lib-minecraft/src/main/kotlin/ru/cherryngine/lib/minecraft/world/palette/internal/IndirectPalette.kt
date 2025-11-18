package ru.cherryngine.lib.minecraft.world.palette.internal

import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap
import it.unimi.dsi.fastutil.ints.IntArrayList

class IndirectPalette(
    bitsPerEntry: Byte,
    count: Int,
    palette: IntArray,
    values: LongArray,
) : MultiValuedPalette {
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
        val newInternalPalette = IndirectPalette(bitsPerEntry, count, IntArray(0), values.clone())
        newInternalPalette.paletteToValueList = IntArrayList(paletteToValueList)
        newInternalPalette.valueToPaletteMap = Int2IntOpenHashMap(valueToPaletteMap)
        return newInternalPalette
    }
}