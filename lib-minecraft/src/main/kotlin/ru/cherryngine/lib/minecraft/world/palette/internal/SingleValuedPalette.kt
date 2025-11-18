package ru.cherryngine.lib.minecraft.world.palette.internal

class SingleValuedPalette(
    var value: Int,
) : InternalPalette {
    override val bitsPerEntry: Byte get() = 0
    override val isEmpty: Boolean get() = value == 0
    override fun clone(): InternalPalette {
        return SingleValuedPalette(value)
    }
}