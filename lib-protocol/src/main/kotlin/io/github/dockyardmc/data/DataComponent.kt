package io.github.dockyardmc.data

import io.github.dockyardmc.protocol.DataComponentHashable

abstract class DataComponent(
    val isSingleField: Boolean = false
) : DataComponentHashable {
    fun getId(): Int {
        return getIdOrNull() ?: throw kotlin.NoSuchElementException("Data Component Registry does not have this component")
    }

    fun getIdOrNull(): Int? {
        return DataComponentRegistry.dataComponentsByIdReversed.getOrDefault(this::class, null)
    }
}