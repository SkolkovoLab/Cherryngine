package ru.cherryngine.lib.minecraft.world.palette.internal

sealed interface InternalPalette {
    val bitsPerEntry: Byte
    val isEmpty: Boolean

    fun clone(): InternalPalette
}

sealed interface MultiValuedPalette : InternalPalette {
    var count: Int
    var values: LongArray
}