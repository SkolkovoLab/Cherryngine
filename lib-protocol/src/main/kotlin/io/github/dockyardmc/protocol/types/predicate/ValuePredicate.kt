package io.github.dockyardmc.protocol.types.predicate

import io.github.dockyardmc.codec.ActionStreamCodec
import io.github.dockyardmc.tide.stream.StreamCodec
import java.util.function.Predicate

sealed interface ValuePredicate : Predicate<String?> {
    companion object {
        val STREAM_CODEC = ActionStreamCodec(
            StreamCodec.INT_BOOLEAN,
            ActionStreamCodec.Entry(Exact::class, Exact.STREAM_CODEC),
            ActionStreamCodec.Entry(Range::class, Range.STREAM_CODEC)
        )
    }

    data class Exact(
        val value: String
    ) : ValuePredicate {
        override fun test(prop: String?): Boolean {
            return prop != null && prop == value
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING, Exact::value,
                ::Exact
            )
        }
    }

    data class Range(
        val min: String?,
        val max: String?
    ) : ValuePredicate {
        override fun test(prop: String?): Boolean {
            if (prop == null || (min == null && max == null)) return false
            try {
                // try to match ints
                val value = prop.toInt()
                return (min == null || value >= min.toInt()) && (max == null || value < max.toInt())
            } catch (ex: NumberFormatException) {
                // not an integer, compare strings
                return (min == null || prop >= min) && (max == null || prop < max)
            }
        }

        companion object {
            val STREAM_CODEC = StreamCodec.of(
                StreamCodec.STRING.optional(), Range::min,
                StreamCodec.STRING.optional(), Range::max,
                ::Range
            )
        }
    }
}