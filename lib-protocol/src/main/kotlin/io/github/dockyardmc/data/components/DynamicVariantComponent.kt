package io.github.dockyardmc.data.components

import io.github.dockyardmc.data.DataComponent
import io.github.dockyardmc.registry.Registry
import io.github.dockyardmc.registry.RegistryEntry

abstract class DynamicVariantComponent<T : RegistryEntry>(
    internal val entry: T,
    val registry: Registry<*>
) : DataComponent()