package ru.cherryngine.lib.minecraft.tide.codec

import io.netty.buffer.ByteBuf
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils.toByteArraySafe
import ru.cherryngine.lib.minecraft.tide.codec.CodecUtils.toByteBuf
import ru.cherryngine.lib.minecraft.tide.transcoder.Transcoder
import ru.cherryngine.lib.minecraft.tide.types.Either
import java.util.*

interface Codec<T> {
    fun <D> encode(transcoder: Transcoder<D>, value: T): D
    fun <D> decode(transcoder: Transcoder<D>, value: D): T

    fun <S> transform(from: (T) -> S, to: (S) -> T): TransformativeCodec<T, S> {
        return TransformativeCodec<T, S>(this, from, to)
    }

    fun list(): Codec<List<T>> {
        return ListCodec<T>(this)
    }

    fun <V> mapTo(valueCodec: Codec<V>): Codec<Map<T, V>> {
        return MapCodec<T, V>(this, valueCodec)
    }

    fun optional(): Codec<T?> {
        return OptionalCodec<T>(this)
    }

    fun default(default: T): Codec<T> {
        return DefaultCodec<T>(this, default)
    }

    fun <R> union(serializers: (T) -> StructCodec<out R>, keyFunc: (R) -> T): StructCodec<R> {
        return union("type", this, serializers, keyFunc)
    }

    fun <R> union(keyCodec: Codec<T>, serializers: (T) -> StructCodec<out R>, keyFunc: (R) -> T): StructCodec<R> {
        return union("type", keyCodec, serializers, keyFunc)
    }

    fun <R> union(keyFiled: String, keyCodec: Codec<T>, serializers: (T) -> StructCodec<out R>, keyFunc: (R) -> T): StructCodec<R> {
        return UnionCodec<T, R>(keyFiled, keyCodec, serializers, keyFunc)
    }

    class EncodingException(override val message: String) : Exception()
    class DecodingException(override val message: String) : Exception()

    companion object {
        val BOOLEAN: Codec<Boolean> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeBoolean(value) },
            { transcoder, value -> transcoder.decodeBoolean(value) }
        )

        val BYTE: Codec<Byte> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeByte(value) },
            { transcoder, value -> transcoder.decodeByte(value) }
        )

        val SHORT: Codec<Short> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeShort(value) },
            { transcoder, value -> transcoder.decodeShort(value) }
        )

        val INT: Codec<Int> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeInt(value) },
            { transcoder, value -> transcoder.decodeInt(value) }
        )

        val LONG: Codec<Long> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeLong(value) },
            { transcoder, value -> transcoder.decodeLong(value) }
        )

        val FLOAT: Codec<Float> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeFloat(value) },
            { transcoder, value -> transcoder.decodeFloat(value) }
        )

        val DOUBLE: Codec<Double> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeDouble(value) },
            { transcoder, value -> transcoder.decodeDouble(value) }
        )

        val STRING: Codec<String> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeString(value) },
            { transcoder, value -> transcoder.decodeString(value) }
        )

        val KEY: Codec<Key> = STRING.transform<Key>(Key::key, Key::asString)

        val BYTE_ARRAY: Codec<ByteArray> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeByteArray(value) },
            { transcoder, value -> transcoder.decodeByteArray(value) }
        )

        val BYTE_BUFFER: Codec<ByteBuf> = BYTE_ARRAY.transform({ from -> from.toByteBuf() }, { to -> to.toByteArraySafe() })

        val INT_ARRAY: Codec<IntArray> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeIntArray(value) },
            { transcoder, value -> transcoder.decodeIntArray(value) }
        )

        val LONG_ARRAY: Codec<LongArray> = PrimitiveCodec(
            { transcoder, value -> transcoder.encodeLongArray(value) },
            { transcoder, value -> transcoder.decodeLongArray(value) }
        )

        val UUID: Codec<UUID> = INT_ARRAY.transform(CodecUtils::intArrayToUuid, CodecUtils::uuidToIntArray)

        val UUID_STRING: Codec<UUID> = STRING.transform(java.util.UUID::fromString, java.util.UUID::toString)

        inline fun <reified E : Enum<E>> enum(): Codec<E> {
            return STRING.transform(
                { value -> E::class.java.enumConstants.first { const -> const.name == value.uppercase() } },
                { value -> value.name.lowercase() }
            )
        }

        fun <L, R> either(leftCodec: Codec<L>, rightCodec: Codec<R>): Codec<Either<L, R>> {
            return EitherCodec(leftCodec, rightCodec)
        }

        fun <T> recursive(self: (Codec<T>) -> Codec<T>): Codec<T> {
            return RecursiveCodec<T>(self).delegate
        }

        fun <T> forwardRef(supplier: () -> Codec<T>): ForwardRefCodec<T> {
            return ForwardRefCodec<T>(supplier)
        }
    }

    private class PrimitiveCodec<T>(
        private val encoder: (Transcoder<Any?>, T) -> Any?,
        private val decoder: (Transcoder<Any?>, Any?) -> T
    ) : Codec<T> {

        @Suppress("UNCHECKED_CAST")
        override fun <D> encode(transcoder: Transcoder<D>, value: T): D {
            return encoder.invoke(transcoder as Transcoder<Any?>, value) as D
        }


        @Suppress("UNCHECKED_CAST")
        override fun <D> decode(transcoder: Transcoder<D>, value: D): T {
            return decoder.invoke(transcoder as Transcoder<Any?>, value)
        }
    }
}