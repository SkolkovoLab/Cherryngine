package ru.cherryngine.lib.minecraft.entity

class MetadataContainer(
    val entries: MutableMap<Int, MetadataEntry<*>> = hashMapOf()
) {
    operator fun <T, K> set(metaField: MetadataDef.MetaField<T, K>, value: K) {
        val index = metaField.index
        val entry: MetadataEntry<T> = metaField.function.entry(metaField.mapper2(value))
        entries[index] = entry
    }

    operator fun contains(metaField: MetadataDef.MetaField<*, *>) = entries.containsKey(metaField.index)

    operator fun <T, K> get(metaField: MetadataDef.MetaField<T, K>): K {
        val index = metaField.index


        @Suppress("UNCHECKED_CAST")
        val entry = entries[index] as MetadataEntry<T>? ?: return metaField.defaultValue

        return metaField.mapper1(entry.value)
    }

    fun remove(metaField: MetadataDef.MetaField<*, *>) {
        val index = metaField.index
        entries.remove(index)
    }
}