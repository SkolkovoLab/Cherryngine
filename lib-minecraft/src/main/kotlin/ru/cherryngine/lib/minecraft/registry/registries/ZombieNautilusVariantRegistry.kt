package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.ZombieNautilusVariant

object ZombieNautilusVariantRegistry : DataDrivenRegistry<ZombieNautilusVariant>(
    "minecraft:zombie_nautilus_variant",
    "zombie_nautilus_variant.json",
    ZombieNautilusVariant.serializer()
)
