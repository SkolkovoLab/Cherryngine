package ru.cherryngine.lib.minecraft.data.components

import ru.cherryngine.lib.minecraft.data.DataComponent
import ru.cherryngine.lib.minecraft.r2.Registry

abstract class DynamicVariantComponent<T : Any>(
    internal val entry: T,
    val registry: Registry<T>,
) : DataComponent()