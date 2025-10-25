package io.github.dockyardmc.world.palette

import io.github.dockyardmc.tide.stream.StreamCodec
import io.netty.buffer.ByteBuf
import java.util.function.IntUnaryOperator

interface Palette {
    operator fun get(x: Int, y: Int, z: Int): Int
    fun getAll(consumer: EntryConsumer)
    fun getAllPresent(consumer: EntryConsumer)
    operator fun set(x: Int, y: Int, z: Int, value: Int)
    fun fill(value: Int)
    fun setAll(supplier: EntrySupplier)
    fun replace(x: Int, y: Int, z: Int, operator: IntUnaryOperator)
    fun replaceAll(function: EntryFunction)

    fun count(): Int

    fun bitsPerEntry(): Int
    fun maxBitsPerEntry(): Int
    fun dimension(): Int

    fun maxSize(): Int {
        val dimension = dimension()
        return dimension * dimension * dimension
    }

    fun clone(): Palette

    fun interface EntrySupplier {
        operator fun get(x: Int, y: Int, z: Int): Int
    }

    fun interface EntryConsumer {
        fun accept(x: Int, y: Int, z: Int, value: Int)
    }

    fun interface EntryFunction {
        fun apply(x: Int, y: Int, z: Int, value: Int): Int
    }

    companion object {
        fun blocks(): Palette = newPalette(16, 8, 4)

        fun biomes(): Palette = newPalette(4, 3, 1)

        fun newPalette(dimension: Int, maxBitsPerEntry: Int, bitsPerEntry: Int): Palette =
            AdaptivePalette(dimension.toByte(), maxBitsPerEntry.toByte(), bitsPerEntry.toByte())

        val STREAM_CODEC = object : StreamCodec<Palette> {
            override fun write(buffer: ByteBuf, value: Palette) {
                if (value is AdaptivePalette) {
                    val optimized: SpecializedPalette = value.optimizedPalette()
                    value.palette = optimized
                    this.write(buffer, optimized)
                }

                if (value is FilledPalette) {
                    buffer.writeByte(0)
                    StreamCodec.VAR_INT.write(buffer, value.value)
                }

                if (value is FlexiblePalette) {
                    buffer.writeByte(value.bitsPerEntry())
                    if (value.bitsPerEntry() <= value.maxBitsPerEntry()) {
                        StreamCodec.VAR_INT.list().write(buffer, value.paletteToValueList)
                    }
                    value.values.forEach { value ->
                        buffer.writeLong(value)
                    }
                }
            }

            override fun read(buffer: ByteBuf): Palette {
                TODO("Not yet implemented")
            }
        }
    }
}
