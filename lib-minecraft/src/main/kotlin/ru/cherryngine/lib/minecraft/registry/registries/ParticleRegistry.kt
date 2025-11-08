package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

class ParticleRegistry : DataDrivenRegistry<Particle>() {
    override val identifier: String = "minecraft:particle"
}

@Serializable
data class Particle(
    val identifier: String,
    val overrideLimiter: Boolean
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }
}