package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

object ParticleRegistry : DataDrivenRegistry<Particle>() {
    override val identifier: String = "minecraft:particle"
    val STREAM_CODEC = RegistryStreamCodec(this)
}

@Serializable
data class Particle(
    val identifier: String,
    val overrideLimiter: Boolean
) : RegistryEntry {

    override fun getProtocolId(): Int {
        return ParticleRegistry.getProtocolIdByEntry(this)
    }

    override fun getEntryIdentifier(): String {
        return identifier
    }
}