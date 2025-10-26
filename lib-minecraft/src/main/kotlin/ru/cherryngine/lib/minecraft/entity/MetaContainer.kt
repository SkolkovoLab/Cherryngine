package ru.cherryngine.lib.minecraft.entity

class MetaContainer {
    val entries: MutableMap<Int, Metadata.Entry<*>> = hashMapOf()

    operator fun <T> set(metaField: MetadataDef.MetaField<T>, value: T) {
        val index = metaField.index
        val entry = metaField.function(value)
        entries[index] = entry
    }

    operator fun contains(metaField: MetadataDef.MetaField<*>) = entries.containsKey(metaField.index)

    operator fun <T> get(metaField: MetadataDef.MetaField<T>): T {
        val index = metaField.index

        val entry = entries[index] ?: return metaField.defaultValue

        @Suppress("UNCHECKED_CAST")
        return entry.value as T
    }

    fun remove(metaField: MetadataDef.MetaField<*>) {
        val index = metaField.index
        entries.remove(index)
    }
}