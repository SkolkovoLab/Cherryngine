package ru.cherryngine.lib.minecraft.world.palette.internal

class DirectPalette(
    bitsPerEntry: Byte,
    count: Int,
    values: LongArray,
) : MultiValuedPalette {
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
        return DirectPalette(bitsPerEntry, count, values.clone())
    }
}