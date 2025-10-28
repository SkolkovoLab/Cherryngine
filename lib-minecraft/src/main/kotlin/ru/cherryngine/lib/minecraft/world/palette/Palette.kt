package ru.cherryngine.lib.minecraft.world.palette

import io.netty.buffer.ByteBuf
import org.jetbrains.annotations.ApiStatus
import ru.cherryngine.lib.minecraft.tide.stream.StreamCodec
import ru.cherryngine.lib.minecraft.utils.bitsToRepresent
import java.util.function.IntUnaryOperator

/**
 * Represents a palette used to store blocks and biomes.
 *
 * 0 is the default value.
 */
sealed interface Palette {
    companion object {
        const val BLOCK_DIMENSION = 16
        const val BLOCK_PALETTE_MIN_BITS = 4
        const val BLOCK_PALETTE_MAX_BITS = 8
        const val BLOCK_PALETTE_DIRECT_BITS = 15

        const val BIOME_DIMENSION = 4
        const val BIOME_PALETTE_MIN_BITS = 1
        const val BIOME_PALETTE_MAX_BITS = 3

        @ApiStatus.Internal
        const val BIOME_PALETTE_DIRECT_BITS = 6 // Vary based on biome count, this is just a sensible default

        fun blocks(bitsPerEntry: Int): Palette =
            sized(
                BLOCK_DIMENSION,
                BLOCK_PALETTE_MIN_BITS,
                BLOCK_PALETTE_MAX_BITS,
                BLOCK_PALETTE_DIRECT_BITS,
                bitsPerEntry
            )

        fun biomes(bitsPerEntry: Int): Palette =
            sized(
                BIOME_DIMENSION,
                BIOME_PALETTE_MIN_BITS,
                BIOME_PALETTE_MAX_BITS,
                BIOME_PALETTE_DIRECT_BITS,
                bitsPerEntry
            )

        fun blocks(): Palette =
            empty(BLOCK_DIMENSION, BLOCK_PALETTE_MIN_BITS, BLOCK_PALETTE_MAX_BITS, BLOCK_PALETTE_DIRECT_BITS)

        fun biomes(): Palette =
            empty(BIOME_DIMENSION, BIOME_PALETTE_MIN_BITS, BIOME_PALETTE_MAX_BITS, BIOME_PALETTE_DIRECT_BITS)

        fun empty(dimension: Int, minBitsPerEntry: Int, maxBitsPerEntry: Int, directBits: Int): Palette =
            PaletteImpl(dimension.toByte(), minBitsPerEntry.toByte(), maxBitsPerEntry.toByte(), directBits.toByte())

        fun sized(
            dimension: Int,
            minBitsPerEntry: Int,
            maxBitsPerEntry: Int,
            directBits: Int,
            bitsPerEntry: Int,
        ): Palette =
            PaletteImpl(
                dimension.toByte(),
                minBitsPerEntry.toByte(),
                maxBitsPerEntry.toByte(),
                directBits.toByte(),
                bitsPerEntry.toByte()
            )

        val BLOCK_SERIALIZER: StreamCodec<Palette> = serializer(
            BLOCK_DIMENSION, BLOCK_PALETTE_MIN_BITS, BLOCK_PALETTE_MAX_BITS, BLOCK_PALETTE_DIRECT_BITS
        )

        fun biomeSerializer(biomeCount: Int): StreamCodec<Palette> {
            val directBits = bitsToRepresent(biomeCount)
            return serializer(BIOME_DIMENSION, BIOME_PALETTE_MIN_BITS, BIOME_PALETTE_MAX_BITS, directBits)
        }

        @Suppress("UNCHECKED_CAST")
        fun serializer(dimension: Int, minIndirect: Int, maxIndirect: Int, directBits: Int): StreamCodec<Palette> {
            return object : StreamCodec<PaletteImpl> {
                override fun write(buffer: ByteBuf, value: PaletteImpl) {
                    // Temporary fix for biome direct bits depending on the number of registered biomes
                    var v: PaletteImpl = value
                    if (directBits != v.directBits.toInt() && !v.hasPalette()) {
                        val tmp = PaletteImpl(
                            dimension.toByte(),
                            minIndirect.toByte(),
                            maxIndirect.toByte(),
                            directBits.toByte()
                        )
                        tmp.setAll { x, y, z -> v.get(x, y, z) }
                        v = tmp
                    }
                    val bitsPerEntry: Byte = v.bitsPerEntry
                    StreamCodec.BYTE.write(buffer, bitsPerEntry)
                    if (bitsPerEntry.toInt() == 0) {
                        StreamCodec.VAR_INT.write(buffer, v.count)
                    } else {
                        if (v.hasPalette()) {
                            StreamCodec.VAR_INT.list().write(buffer, v.paletteToValueList!!)
                        }
                        val vals = v.values
                        if (vals != null) {
                            for (l in vals) StreamCodec.LONG.write(buffer, l)
                        }
                    }
                }

                override fun read(buffer: ByteBuf): PaletteImpl {
                    val bitsPerEntry = StreamCodec.BYTE.read(buffer)
                    if (bitsPerEntry.toInt() == 0) {
                        // Single value palette
                        val value = StreamCodec.VAR_INT.read(buffer)
                        val palette = PaletteImpl(
                            dimension.toByte(),
                            minIndirect.toByte(),
                            maxIndirect.toByte(),
                            directBits.toByte()
                        )
                        palette.count = value
                        return palette
                    } else if (bitsPerEntry.toInt() in minIndirect..maxIndirect) {
                        // Indirect palette
                        val palette = StreamCodec.VAR_INT_ARRAY.read(buffer)
                        val entriesPerLong = 64 / bitsPerEntry.toInt()
                        val data = LongArray((dimension * dimension * dimension) / entriesPerLong + 1)
                        for (i in data.indices) data[i] = StreamCodec.LONG.read(buffer)
                        return PaletteImpl(
                            dimension.toByte(),
                            minIndirect.toByte(),
                            maxIndirect.toByte(),
                            directBits.toByte(),
                            bitsPerEntry,
                            Palettes.count(bitsPerEntry.toInt(), data),
                            palette,
                            data
                        )
                    } else {
                        // Direct palette
                        val length = Palettes.arrayLength(dimension, bitsPerEntry.toInt())
                        val data = LongArray(length)
                        for (i in data.indices) data[i] = StreamCodec.LONG.read(buffer)
                        return PaletteImpl(
                            dimension.toByte(),
                            minIndirect.toByte(),
                            maxIndirect.toByte(),
                            directBits.toByte(),
                            bitsPerEntry,
                            Palettes.count(bitsPerEntry.toInt(), data),
                            IntArray(0),
                            data
                        )
                    }
                }
            } as StreamCodec<Palette>
        }
    }

    operator fun get(x: Int, y: Int, z: Int): Int

    fun getAll(consumer: EntryConsumer)

    fun getAllPresent(consumer: EntryConsumer)

    operator fun set(x: Int, y: Int, z: Int, value: Int)

    fun fill(value: Int)

    fun offset(offset: Int)

    fun replace(oldValue: Int, newValue: Int)

    fun setAll(supplier: EntrySupplier)

    fun replace(x: Int, y: Int, z: Int, operator: IntUnaryOperator)

    fun replaceAll(function: EntryFunction)

    /**
     * Efficiently copies values from another palette with the given offset.
     * Both palettes must have the same dimension.
     */
    fun copyFrom(source: Palette, offsetX: Int, offsetY: Int, offsetZ: Int)

    /**
     * Efficiently copies values from another palette starting at position (0, 0, 0).
     * Convenience for copyFrom(source, 0, 0, 0).
     */
    fun copyFrom(source: Palette)

    /**
     * Returns the number of entries in this palette.
     */
    fun count(): Int

    /**
     * Returns the number of entries in this palette that match the given value.
     */
    fun count(value: Int): Int

    /**
     * Checks if the palette contains the given value.
     */
    fun any(value: Int): Boolean

    /**
     * Returns the number of bits used per entry.
     */
    fun bitsPerEntry(): Int

    fun dimension(): Int

    /**
     * Returns the maximum number of entries in this palette.
     */
    fun maxSize(): Int {
        val dimension = dimension()
        return dimension * dimension * dimension
    }

    fun optimize(focus: Optimization)

    enum class Optimization { SIZE, SPEED }

    /**
     * Compare palettes content independently of their storage format.
     */
    fun compare(palette: Palette): Boolean

    fun clone(): Palette

    @ApiStatus.Internal
    fun paletteIndexToValue(value: Int): Int

    @ApiStatus.Internal
    fun valueToPaletteIndex(value: Int): Int

    /**
     * Gets the single value of this palette if it is a single value palette, otherwise returns -1.
     */
    @ApiStatus.Internal
    fun singleValue(): Int

    /**
     * Gets the value array if it has one, otherwise returns null (i.e. single value palette).
     */
    @ApiStatus.Internal
    fun indexedValues(): LongArray?

    fun interface EntrySupplier {
        fun get(x: Int, y: Int, z: Int): Int
    }

    fun interface EntryConsumer {
        fun accept(x: Int, y: Int, z: Int, value: Int)
    }

    fun interface EntryFunction {
        fun apply(x: Int, y: Int, z: Int, value: Int): Int
    }
}
