package io.github.dockyardmc.tide.stream

import io.github.dockyardmc.tide.codec.CodecUtils
import io.github.dockyardmc.tide.codec.CodecUtils.toByteArraySafe
import io.netty.buffer.ByteBuf
import kotlinx.datetime.Instant
import net.kyori.adventure.key.Key
import java.util.*
import kotlin.experimental.and
import kotlin.experimental.or
import java.util.UUID as JavaUUID

interface StreamCodec<T> {
    fun write(buffer: ByteBuf, value: T)
    fun read(buffer: ByteBuf): T

    fun optional(): OptionalStreamCodec<T> {
        return OptionalStreamCodec<T>(this)
    }

    fun default(default: T): DefaultStreamCodec<T> {
        return DefaultStreamCodec<T>(this, default)
    }

    fun <S> transform(to: (T) -> S, from: (S) -> T): TransformativeStreamCodec<T, S> {
        return TransformativeStreamCodec<T, S>(this, to, from)
    }

    fun <V> mapTo(valueCodec: StreamCodec<V>): MapStreamCodec<T, V> {
        return MapStreamCodec<T, V>(this, valueCodec)
    }

    fun list(): ListStreamCodec<T> {
        return ListStreamCodec<T>(this)
    }

    fun <R, TR : R> union(serializers: (T) -> StreamCodec<out TR>, keyFunc: (R) -> T): UnionStreamCodec<TR, T, TR> {
        return UnionStreamCodec(this, keyFunc, serializers)
    }

    fun wrapped(): WrappedStreamCodec<T> {
        return WrappedStreamCodec<T>(this)
    }

    companion object {
        val UNIT = object : StreamCodec<Unit> {
            override fun write(buffer: ByteBuf, value: Unit) {}

            override fun read(buffer: ByteBuf) {}
        }
        val BOOLEAN = object : StreamCodec<Boolean> {
            override fun write(buffer: ByteBuf, value: Boolean) {
                buffer.writeBoolean(value)
            }

            override fun read(buffer: ByteBuf): Boolean {
                return buffer.readBoolean()
            }
        }
        val INT_BOOLEAN = BOOLEAN.transform<Int>({ if (it) 1 else 0 }, { it > 0 })
        val BYTE = object : StreamCodec<Byte> {
            override fun write(buffer: ByteBuf, value: Byte) {
                buffer.writeByte(value.toInt())
            }

            override fun read(buffer: ByteBuf): Byte {
                return buffer.readByte()
            }
        }
        val INT_BYTE = BYTE.transform<Int>(Byte::toInt, Int::toByte)
        val SHORT = object : StreamCodec<Short> {
            override fun write(buffer: ByteBuf, value: Short) {
                buffer.writeShort(value.toInt())
            }

            override fun read(buffer: ByteBuf): Short {
                return buffer.readShort()
            }
        }
        val INT_SHORT = SHORT.transform<Int>(Short::toInt, Int::toShort)
        val INT = object : StreamCodec<Int> {
            override fun write(buffer: ByteBuf, value: Int) {
                buffer.writeInt(value)
            }

            override fun read(buffer: ByteBuf): Int {
                return buffer.readInt()
            }
        }
        val VAR_INT = object : StreamCodec<Int> {
            override fun write(buffer: ByteBuf, value: Int) {
                CodecUtils.writeVarInt(buffer, value)
            }

            override fun read(buffer: ByteBuf): Int {
                return CodecUtils.readVarInt(buffer)
            }
        }
        val LONG = object : StreamCodec<Long> {
            override fun write(buffer: ByteBuf, value: Long) {
                buffer.writeLong(value)
            }

            override fun read(buffer: ByteBuf): Long {
                return buffer.readLong()
            }
        }
        val VAR_LONG = object : StreamCodec<Long> {
            override fun write(buffer: ByteBuf, value: Long) {
                CodecUtils.writeVarLong(buffer, value)
            }

            override fun read(buffer: ByteBuf): Long {
                return CodecUtils.readVarLong(buffer)
            }
        }
        val FLOAT = object : StreamCodec<Float> {
            override fun write(buffer: ByteBuf, value: Float) {
                buffer.writeFloat(value)
            }

            override fun read(buffer: ByteBuf): Float {
                return buffer.readFloat()
            }
        }
        val DOUBLE = object : StreamCodec<Double> {
            override fun write(buffer: ByteBuf, value: Double) {
                buffer.writeDouble(value)
            }

            override fun read(buffer: ByteBuf): Double {
                return buffer.readDouble()
            }
        }
        val DOUBLE_FLOAT = FLOAT.transform<Double>(Float::toDouble, Double::toFloat)
        val STRING = StringStreamCodec()
        val KEY = STRING.transform<Key>(Key::key, Key::asString)
        val KEY_TWO_STRINGS = of(STRING, Key::namespace, STRING, Key::value, Key::key)
        val RAW_BYTES = object : StreamCodec<ByteBuf> {
            override fun write(buffer: ByteBuf, value: ByteBuf) {
                buffer.writeBytes(value)
            }

            override fun read(buffer: ByteBuf): ByteBuf {
                return buffer.readBytes(buffer.readableBytes())
            }
        }
        val RAW_BYTES_ARRAY = object : StreamCodec<ByteArray> {
            override fun write(buffer: ByteBuf, value: ByteArray) {
                buffer.writeBytes(value)
            }

            override fun read(buffer: ByteBuf): ByteArray {
                val len = buffer.readableBytes()
                val b = buffer.readBytes(len)
                val byteArray = b.toByteArraySafe()
                b.release()
                return byteArray
            }
        }
        val BYTE_ARRAY = object : StreamCodec<ByteArray> {
            override fun write(buffer: ByteBuf, value: ByteArray) {
                VAR_INT.write(buffer, value.size)
                buffer.writeBytes(value)
            }

            override fun read(buffer: ByteBuf): ByteArray {
                val len = VAR_INT.read(buffer)
                val b = buffer.readBytes(len)
                val byteArray = b.toByteArraySafe()
                b.release()
                return byteArray
            }
        }
        val BYTE_BUF = object : StreamCodec<ByteBuf> {
            override fun write(buffer: ByteBuf, value: ByteBuf) {
                VAR_INT.write(buffer, value.readableBytes())
                RAW_BYTES.write(buffer, value)
            }

            override fun read(buffer: ByteBuf): ByteBuf {
                val size = VAR_INT.read(buffer)
                return buffer.readBytes(size)
            }
        }
        val UUID = object : StreamCodec<JavaUUID> {
            override fun write(buffer: ByteBuf, value: JavaUUID) {
                LONG.write(buffer, value.mostSignificantBits)
                LONG.write(buffer, value.leastSignificantBits)
            }

            override fun read(buffer: ByteBuf): JavaUUID {
                val mostSignificant = LONG.read(buffer)
                val leastSignificant = LONG.read(buffer)
                return JavaUUID(mostSignificant, leastSignificant)
            }
        }
        val LONG_ARRAY = object : StreamCodec<LongArray> {
            override fun write(buffer: ByteBuf, value: LongArray) {
                VAR_INT.write(buffer, value.size)
                value.forEach { buffer.writeLong(it) }
            }

            override fun read(buffer: ByteBuf): LongArray {
                val size = VAR_INT.read(buffer)
                return LongArray(size) { buffer.readLong() }
            }
        }
        val UUID_STRING = STRING.transform<JavaUUID>(JavaUUID::fromString, JavaUUID::toString)
        val BIT_SET = LONG_ARRAY.transform<BitSet>(BitSet::valueOf, BitSet::toLongArray)
        val INSTANT = LONG.transform<Instant>(Instant::fromEpochMilliseconds, Instant::toEpochMilliseconds)

        inline fun <reified E : Enum<E>> enum(): StreamCodec<E> {
            return EnumStreamCodec<E>(E::class)
        }

        inline fun <reified E : Enum<E>> enumString(): StreamCodec<E> {
            return STRING.transform<E>(
                { string -> enumValueOf<E>(string) }
            ) { enum -> enum.name.lowercase() }
        }

        fun fixedBitSet(length: Int): FixedBitSetStreamCodec {
            return FixedBitSetStreamCodec(length)
        }

        fun <T> lengthPrefixed(codec: StreamCodec<T>): StreamCodec<T> {
            return LengthPrefixedStreamCodec<T>(codec)
        }

        fun <L, R> either(leftCodec: StreamCodec<L>, rightCodec: StreamCodec<R>): EitherStreamCodec<L, R> {
            return EitherStreamCodec<L, R>(leftCodec, rightCodec)
        }

        fun <R> of(
            supplier: () -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {}

            override fun read(buffer: ByteBuf): R {
                return supplier.invoke()
            }
        }

        fun <R, P1> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            supplier: (P1) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                return supplier.invoke(result1)
            }
        }

        fun <R, P1, P2> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            supplier: (P1, P2) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                return supplier.invoke(result1, result2)
            }
        }

        fun <R, P1, P2, P3> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            supplier: (P1, P2, P3) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                return supplier.invoke(result1, result2, result3)
            }
        }

        fun <R, P1, P2, P3, P4> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            supplier: (P1, P2, P3, P4) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                return supplier.invoke(result1, result2, result3, result4)
            }
        }

        fun <R, P1, P2, P3, P4, P5> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            supplier: (P1, P2, P3, P4, P5) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                return supplier.invoke(result1, result2, result3, result4, result5)
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            supplier: (P1, P2, P3, P4, P5, P6) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                return supplier.invoke(result1, result2, result3, result4, result5, result6)
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            supplier: (P1, P2, P3, P4, P5, P6, P7) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                return supplier.invoke(result1, result2, result3, result4, result5, result6, result7)
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                return supplier.invoke(result1, result2, result3, result4, result5, result6, result7, result8)
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                return supplier.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9)
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            codec15: StreamCodec<P15>, getter15: (R) -> P15,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
                codec15.write(buffer, getter15.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                val result15 = codec15.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            codec15: StreamCodec<P15>, getter15: (R) -> P15,
            codec16: StreamCodec<P16>, getter16: (R) -> P16,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
                codec15.write(buffer, getter15.invoke(value))
                codec16.write(buffer, getter16.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                val result15 = codec15.read(buffer)
                val result16 = codec16.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15,
                    result16
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            codec15: StreamCodec<P15>, getter15: (R) -> P15,
            codec16: StreamCodec<P16>, getter16: (R) -> P16,
            codec17: StreamCodec<P17>, getter17: (R) -> P17,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
                codec15.write(buffer, getter15.invoke(value))
                codec16.write(buffer, getter16.invoke(value))
                codec17.write(buffer, getter17.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                val result15 = codec15.read(buffer)
                val result16 = codec16.read(buffer)
                val result17 = codec17.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15,
                    result16,
                    result17
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            codec15: StreamCodec<P15>, getter15: (R) -> P15,
            codec16: StreamCodec<P16>, getter16: (R) -> P16,
            codec17: StreamCodec<P17>, getter17: (R) -> P17,
            codec18: StreamCodec<P18>, getter18: (R) -> P18,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
                codec15.write(buffer, getter15.invoke(value))
                codec16.write(buffer, getter16.invoke(value))
                codec17.write(buffer, getter17.invoke(value))
                codec18.write(buffer, getter18.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                val result15 = codec15.read(buffer)
                val result16 = codec16.read(buffer)
                val result17 = codec17.read(buffer)
                val result18 = codec18.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15,
                    result16,
                    result17,
                    result18
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            codec15: StreamCodec<P15>, getter15: (R) -> P15,
            codec16: StreamCodec<P16>, getter16: (R) -> P16,
            codec17: StreamCodec<P17>, getter17: (R) -> P17,
            codec18: StreamCodec<P18>, getter18: (R) -> P18,
            codec19: StreamCodec<P19>, getter19: (R) -> P19,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
                codec15.write(buffer, getter15.invoke(value))
                codec16.write(buffer, getter16.invoke(value))
                codec17.write(buffer, getter17.invoke(value))
                codec18.write(buffer, getter18.invoke(value))
                codec19.write(buffer, getter19.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                val result15 = codec15.read(buffer)
                val result16 = codec16.read(buffer)
                val result17 = codec17.read(buffer)
                val result18 = codec18.read(buffer)
                val result19 = codec19.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15,
                    result16,
                    result17,
                    result18,
                    result19
                )
            }
        }

        fun <R, P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20> of(
            codec1: StreamCodec<P1>, getter1: (R) -> P1,
            codec2: StreamCodec<P2>, getter2: (R) -> P2,
            codec3: StreamCodec<P3>, getter3: (R) -> P3,
            codec4: StreamCodec<P4>, getter4: (R) -> P4,
            codec5: StreamCodec<P5>, getter5: (R) -> P5,
            codec6: StreamCodec<P6>, getter6: (R) -> P6,
            codec7: StreamCodec<P7>, getter7: (R) -> P7,
            codec8: StreamCodec<P8>, getter8: (R) -> P8,
            codec9: StreamCodec<P9>, getter9: (R) -> P9,
            codec10: StreamCodec<P10>, getter10: (R) -> P10,
            codec11: StreamCodec<P11>, getter11: (R) -> P11,
            codec12: StreamCodec<P12>, getter12: (R) -> P12,
            codec13: StreamCodec<P13>, getter13: (R) -> P13,
            codec14: StreamCodec<P14>, getter14: (R) -> P14,
            codec15: StreamCodec<P15>, getter15: (R) -> P15,
            codec16: StreamCodec<P16>, getter16: (R) -> P16,
            codec17: StreamCodec<P17>, getter17: (R) -> P17,
            codec18: StreamCodec<P18>, getter18: (R) -> P18,
            codec19: StreamCodec<P19>, getter19: (R) -> P19,
            codec20: StreamCodec<P20>, getter20: (R) -> P20,
            supplier: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                codec1.write(buffer, getter1.invoke(value))
                codec2.write(buffer, getter2.invoke(value))
                codec3.write(buffer, getter3.invoke(value))
                codec4.write(buffer, getter4.invoke(value))
                codec5.write(buffer, getter5.invoke(value))
                codec6.write(buffer, getter6.invoke(value))
                codec7.write(buffer, getter7.invoke(value))
                codec8.write(buffer, getter8.invoke(value))
                codec9.write(buffer, getter9.invoke(value))
                codec10.write(buffer, getter10.invoke(value))
                codec11.write(buffer, getter11.invoke(value))
                codec12.write(buffer, getter12.invoke(value))
                codec13.write(buffer, getter13.invoke(value))
                codec14.write(buffer, getter14.invoke(value))
                codec15.write(buffer, getter15.invoke(value))
                codec16.write(buffer, getter16.invoke(value))
                codec17.write(buffer, getter17.invoke(value))
                codec18.write(buffer, getter18.invoke(value))
                codec19.write(buffer, getter19.invoke(value))
                codec20.write(buffer, getter20.invoke(value))
            }

            override fun read(buffer: ByteBuf): R {
                val result1 = codec1.read(buffer)
                val result2 = codec2.read(buffer)
                val result3 = codec3.read(buffer)
                val result4 = codec4.read(buffer)
                val result5 = codec5.read(buffer)
                val result6 = codec6.read(buffer)
                val result7 = codec7.read(buffer)
                val result8 = codec8.read(buffer)
                val result9 = codec9.read(buffer)
                val result10 = codec10.read(buffer)
                val result11 = codec11.read(buffer)
                val result12 = codec12.read(buffer)
                val result13 = codec13.read(buffer)
                val result14 = codec14.read(buffer)
                val result15 = codec15.read(buffer)
                val result16 = codec16.read(buffer)
                val result17 = codec17.read(buffer)
                val result18 = codec18.read(buffer)
                val result19 = codec19.read(buffer)
                val result20 = codec20.read(buffer)
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15,
                    result16,
                    result17,
                    result18,
                    result19,
                    result20
                )
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            supplier: (Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                return supplier.invoke(result1)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            supplier: (Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                return supplier.invoke(result1, result2)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            mask3: Byte, getter3: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                if (getter3(value)) byte = byte or mask3
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                val result3 = (byte and mask3) != 0.toByte()
                return supplier.invoke(result1, result2, result3)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            mask3: Byte, getter3: (R) -> Boolean,
            mask4: Byte, getter4: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                if (getter3(value)) byte = byte or mask3
                if (getter4(value)) byte = byte or mask4
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                val result3 = (byte and mask3) != 0.toByte()
                val result4 = (byte and mask4) != 0.toByte()
                return supplier.invoke(result1, result2, result3, result4)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            mask3: Byte, getter3: (R) -> Boolean,
            mask4: Byte, getter4: (R) -> Boolean,
            mask5: Byte, getter5: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                if (getter3(value)) byte = byte or mask3
                if (getter4(value)) byte = byte or mask4
                if (getter5(value)) byte = byte or mask5
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                val result3 = (byte and mask3) != 0.toByte()
                val result4 = (byte and mask4) != 0.toByte()
                val result5 = (byte and mask5) != 0.toByte()
                return supplier.invoke(result1, result2, result3, result4, result5)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            mask3: Byte, getter3: (R) -> Boolean,
            mask4: Byte, getter4: (R) -> Boolean,
            mask5: Byte, getter5: (R) -> Boolean,
            mask6: Byte, getter6: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                if (getter3(value)) byte = byte or mask3
                if (getter4(value)) byte = byte or mask4
                if (getter5(value)) byte = byte or mask5
                if (getter6(value)) byte = byte or mask6
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                val result3 = (byte and mask3) != 0.toByte()
                val result4 = (byte and mask4) != 0.toByte()
                val result5 = (byte and mask5) != 0.toByte()
                val result6 = (byte and mask6) != 0.toByte()
                return supplier.invoke(result1, result2, result3, result4, result5, result6)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            mask3: Byte, getter3: (R) -> Boolean,
            mask4: Byte, getter4: (R) -> Boolean,
            mask5: Byte, getter5: (R) -> Boolean,
            mask6: Byte, getter6: (R) -> Boolean,
            mask7: Byte, getter7: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                if (getter3(value)) byte = byte or mask3
                if (getter4(value)) byte = byte or mask4
                if (getter5(value)) byte = byte or mask5
                if (getter6(value)) byte = byte or mask6
                if (getter7(value)) byte = byte or mask7
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                val result3 = (byte and mask3) != 0.toByte()
                val result4 = (byte and mask4) != 0.toByte()
                val result5 = (byte and mask5) != 0.toByte()
                val result6 = (byte and mask6) != 0.toByte()
                val result7 = (byte and mask7) != 0.toByte()
                return supplier.invoke(result1, result2, result3, result4, result5, result6, result7)
            }
        }

        fun <R> byteFlags(
            mask1: Byte, getter1: (R) -> Boolean,
            mask2: Byte, getter2: (R) -> Boolean,
            mask3: Byte, getter3: (R) -> Boolean,
            mask4: Byte, getter4: (R) -> Boolean,
            mask5: Byte, getter5: (R) -> Boolean,
            mask6: Byte, getter6: (R) -> Boolean,
            mask7: Byte, getter7: (R) -> Boolean,
            mask8: Byte, getter8: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var byte: Byte = 0
                if (getter1(value)) byte = byte or mask1
                if (getter2(value)) byte = byte or mask2
                if (getter3(value)) byte = byte or mask3
                if (getter4(value)) byte = byte or mask4
                if (getter5(value)) byte = byte or mask5
                if (getter6(value)) byte = byte or mask6
                if (getter7(value)) byte = byte or mask7
                if (getter8(value)) byte = byte or mask8
                buffer.writeByte(byte.toInt())
            }

            override fun read(buffer: ByteBuf): R {
                val byte = buffer.readByte()
                val result1 = (byte and mask1) != 0.toByte()
                val result2 = (byte and mask2) != 0.toByte()
                val result3 = (byte and mask3) != 0.toByte()
                val result4 = (byte and mask4) != 0.toByte()
                val result5 = (byte and mask5) != 0.toByte()
                val result6 = (byte and mask6) != 0.toByte()
                val result7 = (byte and mask7) != 0.toByte()
                val result8 = (byte and mask8) != 0.toByte()
                return supplier.invoke(result1, result2, result3, result4, result5, result6, result7, result8)
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                return supplier.invoke(result1, result2, result3, result4, result5, result6, result7, result8, result9)
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10
                )
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            mask11: Int, getter11: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                if (getter11(value)) int = int or mask11
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                val result11 = (int and mask11) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11
                )
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            mask11: Int, getter11: (R) -> Boolean,
            mask12: Int, getter12: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                if (getter11(value)) int = int or mask11
                if (getter12(value)) int = int or mask12
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                val result11 = (int and mask11) != 0
                val result12 = (int and mask12) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12
                )
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            mask11: Int, getter11: (R) -> Boolean,
            mask12: Int, getter12: (R) -> Boolean,
            mask13: Int, getter13: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                if (getter11(value)) int = int or mask11
                if (getter12(value)) int = int or mask12
                if (getter13(value)) int = int or mask13
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                val result11 = (int and mask11) != 0
                val result12 = (int and mask12) != 0
                val result13 = (int and mask13) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13
                )
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            mask11: Int, getter11: (R) -> Boolean,
            mask12: Int, getter12: (R) -> Boolean,
            mask13: Int, getter13: (R) -> Boolean,
            mask14: Int, getter14: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                if (getter11(value)) int = int or mask11
                if (getter12(value)) int = int or mask12
                if (getter13(value)) int = int or mask13
                if (getter14(value)) int = int or mask14
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                val result11 = (int and mask11) != 0
                val result12 = (int and mask12) != 0
                val result13 = (int and mask13) != 0
                val result14 = (int and mask14) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14
                )
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            mask11: Int, getter11: (R) -> Boolean,
            mask12: Int, getter12: (R) -> Boolean,
            mask13: Int, getter13: (R) -> Boolean,
            mask14: Int, getter14: (R) -> Boolean,
            mask15: Int, getter15: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                if (getter11(value)) int = int or mask11
                if (getter12(value)) int = int or mask12
                if (getter13(value)) int = int or mask13
                if (getter14(value)) int = int or mask14
                if (getter15(value)) int = int or mask15
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                val result11 = (int and mask11) != 0
                val result12 = (int and mask12) != 0
                val result13 = (int and mask13) != 0
                val result14 = (int and mask14) != 0
                val result15 = (int and mask15) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15
                )
            }
        }

        fun <R> intFlags(
            mask1: Int, getter1: (R) -> Boolean,
            mask2: Int, getter2: (R) -> Boolean,
            mask3: Int, getter3: (R) -> Boolean,
            mask4: Int, getter4: (R) -> Boolean,
            mask5: Int, getter5: (R) -> Boolean,
            mask6: Int, getter6: (R) -> Boolean,
            mask7: Int, getter7: (R) -> Boolean,
            mask8: Int, getter8: (R) -> Boolean,
            mask9: Int, getter9: (R) -> Boolean,
            mask10: Int, getter10: (R) -> Boolean,
            mask11: Int, getter11: (R) -> Boolean,
            mask12: Int, getter12: (R) -> Boolean,
            mask13: Int, getter13: (R) -> Boolean,
            mask14: Int, getter14: (R) -> Boolean,
            mask15: Int, getter15: (R) -> Boolean,
            mask16: Int, getter16: (R) -> Boolean,
            supplier: (Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean) -> R,
        ) = object : StreamCodec<R> {
            override fun write(buffer: ByteBuf, value: R) {
                var int = 0
                if (getter1(value)) int = int or mask1
                if (getter2(value)) int = int or mask2
                if (getter3(value)) int = int or mask3
                if (getter4(value)) int = int or mask4
                if (getter5(value)) int = int or mask5
                if (getter6(value)) int = int or mask6
                if (getter7(value)) int = int or mask7
                if (getter8(value)) int = int or mask8
                if (getter9(value)) int = int or mask9
                if (getter10(value)) int = int or mask10
                if (getter11(value)) int = int or mask11
                if (getter12(value)) int = int or mask12
                if (getter13(value)) int = int or mask13
                if (getter14(value)) int = int or mask14
                if (getter15(value)) int = int or mask15
                if (getter16(value)) int = int or mask16
                buffer.writeInt(int)
            }

            override fun read(buffer: ByteBuf): R {
                val int = buffer.readInt()
                val result1 = (int and mask1) != 0
                val result2 = (int and mask2) != 0
                val result3 = (int and mask3) != 0
                val result4 = (int and mask4) != 0
                val result5 = (int and mask5) != 0
                val result6 = (int and mask6) != 0
                val result7 = (int and mask7) != 0
                val result8 = (int and mask8) != 0
                val result9 = (int and mask9) != 0
                val result10 = (int and mask10) != 0
                val result11 = (int and mask11) != 0
                val result12 = (int and mask12) != 0
                val result13 = (int and mask13) != 0
                val result14 = (int and mask14) != 0
                val result15 = (int and mask15) != 0
                val result16 = (int and mask16) != 0
                return supplier.invoke(
                    result1,
                    result2,
                    result3,
                    result4,
                    result5,
                    result6,
                    result7,
                    result8,
                    result9,
                    result10,
                    result11,
                    result12,
                    result13,
                    result14,
                    result15,
                    result16
                )
            }
        }
    }
}