package ru.cherryngine.lib.minecraft.data

import ru.cherryngine.lib.minecraft.protocol.DataComponentHashable

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