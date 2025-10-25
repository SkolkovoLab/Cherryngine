package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.registry.Registry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

abstract class DynamicVariantComponent<T : RegistryEntry>(
    internal val entry: T,
    val registry: Registry<*>
) : DataComponent()