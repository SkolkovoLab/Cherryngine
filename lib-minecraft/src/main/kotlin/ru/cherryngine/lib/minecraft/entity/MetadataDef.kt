package ru.cherryngine.lib.minecraft.entity

import kotlin.enums.enumEntries

/**
 * List of all entity metadata.
 * <p>
 * Classes must be used (and not interfaces) to enforce loading order.
 */
@Suppress("unused")
sealed class MetadataDef {
    private var lastIndex = 0

    internal fun <T> index(
        function: MetadataEntry.Type<T>,
        defaultValue: T,
    ): MetaField<T, T> {
        return MetaField(lastIndex++, function, defaultValue, { it }, { it })
    }

    internal fun <T, K> index(
        function: MetadataEntry.Type<T>,
        defaultValue: K,
        mapper1: (T) -> K,
        mapper2: (K) -> T,
    ): MetaField<T, K> {
        return MetaField(lastIndex++, function, defaultValue, mapper1, mapper2)
    }

    class MetaField<T, K>(
        val index: Int,
        val function: MetadataEntry.Type<T>,
        val defaultValue: K,
        val mapper1: (T) -> K,
        val mapper2: (K) -> T,
    )

    internal inline fun <reified T : Enum<T>> fromIndex(index: Int): T {
        return enumEntries<T>()[index]
    }

    internal inline fun <reified T : Enum<T>> fromIndex(index: Byte): T {
        return enumEntries<T>()[index.toInt()]
    }

    internal fun intIndex(value: Enum<*>): Int {
        return value.ordinal
    }

    internal fun byteIndex(value: Enum<*>): Byte {
        return value.ordinal.toByte()
    }
}
