package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.Particle

object ParticleRegistry : DataDrivenRegistry<Particle>(
    "minecraft:particle",
    "registry/particle_registry.json.gz",
    Particle.serializer()
)
