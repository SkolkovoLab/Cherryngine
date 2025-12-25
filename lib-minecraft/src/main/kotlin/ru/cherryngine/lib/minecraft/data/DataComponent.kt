package ru.cherryngine.lib.minecraft.data

abstract class DataComponent(
    val isSingleField: Boolean = false
) {
    fun getId(): Int {
        return DataComponentRegistry.get(this::class).id
    }
}