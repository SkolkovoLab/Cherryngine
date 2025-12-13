package ru.cherryngine.lib.minecraft.registry

abstract class DynamicRegistry<T : RegistryEntry>(
    identifier: String,
) : Registry<T>(identifier) {
    fun updateCache() {
    }
}