package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class Particle(
    val identifier: String,
    val overrideLimiter: Boolean,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }
}