package ru.cherryngine.lib.minecraft.registry.registries.envattr

import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.tide.codec.Codec

data class EnvironmentAttribute<T>(
    val identifier: String,
    val type: EnvironmentAttributeType<T>,
    val default: T,
) : RegistryEntry {
    override fun getEntryIdentifier() = identifier

    companion object {
        val CODEC = Codec.KEY.transform(
            { EnvironmentAttributeRegistry[it] },
            { Key.key(it.identifier) }
        )
    }
}