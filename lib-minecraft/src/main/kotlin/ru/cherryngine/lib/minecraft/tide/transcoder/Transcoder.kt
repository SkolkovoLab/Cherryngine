package ru.cherryngine.lib.minecraft.tide.transcoder

interface Transcoder<T> {
    fun encodeNull(): T

    fun encodeBoolean(value: Boolean): T
    fun decodeBoolean(value: T): Boolean

    fun encodeByte(value: Byte): T
    fun decodeByte(value: T): Byte

    fun encodeShort(value: Short): T
    fun decodeShort(value: T): Short

    fun encodeInt(value: Int): T
    fun decodeInt(value: T): Int

    fun encodeLong(value: Long): T
    fun decodeLong(value: T): Long

    fun encodeFloat(value: Float): T
    fun decodeFloat(value: T): Float

    fun encodeDouble(value: Double): T
    fun decodeDouble(value: T): Double

    fun encodeString(value: String): T
    fun decodeString(value: T): String

    fun encodeList(size: Int): ListBuilder<T>
    fun decodeList(value: T): List<T>
    fun emptyList() = encodeList(0).build()

    fun encodeMap(): VirtualMapBuilder<T>

    fun decodeMap(value: T): VirtualMap<T>
    fun emptyMap() = encodeMap().build()

    fun encodeByteArray(value: ByteArray): T {
        val list = encodeList(value.size)
        value.forEach { byte -> list.add(encodeByte(byte)) }
        return list.build()
    }

    fun decodeByteArray(value: T): ByteArray {
        val bytes = mutableListOf<Byte>()
        decodeList(value).forEach { item ->
            bytes.add(decodeByte(item))
        }
        return bytes.toByteArray()
    }

    fun encodeIntArray(value: IntArray): T {
        val list = encodeList(value.size)
        value.forEach { int -> list.add(encodeInt(int)) }
        return list.build()
    }

    fun decodeIntArray(value: T): IntArray {
        val ints = mutableListOf<Int>()
        decodeList(value).forEach { item ->
            ints.add(decodeInt(item))
        }
        return ints.toIntArray()
    }

    fun encodeLongArray(value: LongArray): T {
        val list = encodeList(value.size)
        value.forEach { int -> list.add(encodeLong(int)) }
        return list.build()
    }

    fun decodeLongArray(value: T): LongArray {
        val longs = mutableListOf<Long>()
        decodeList(value).forEach { item ->
            longs.add(decodeLong(item))
        }
        return longs.toLongArray()
    }

    interface ListBuilder<T> {
        fun add(value: T): ListBuilder<T>
        fun build(): T
    }

    interface VirtualMap<T> {
        fun getKeys(): Collection<String>
        fun hasValue(key: String): Boolean
        fun getValue(key: String): T

        val size get() = getKeys().size
        val isEmpty get() = getKeys().isEmpty()
    }

    interface VirtualMapBuilder<T> {
        fun put(key: T, value: T): VirtualMapBuilder<T>
        fun put(key: String, value: T): VirtualMapBuilder<T>
        fun build(): T
    }
}