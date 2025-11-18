package ru.cherryngine.lib.minecraft.utils.bitstorage

interface BitStorage {
    val size: Int
    val data: LongArray
    val bits: Int

    fun getAndSet(index: Int, value: Int): Int

    operator fun get(index: Int): Int

    operator fun set(index: Int, value: Int)

    fun unpack(output: IntArray)

    fun copy(): BitStorage

    fun checkIndex(index: Int) {
        require(index in 0 until size) { "Index must be between 0 and $size (is $index)" }
    }
}