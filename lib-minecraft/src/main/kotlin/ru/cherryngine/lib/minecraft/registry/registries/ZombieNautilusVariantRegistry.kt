package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.ZombieNautilusVariant

object ZombieNautilusVariantRegistry : KtJsonDataDrivenRegistry<ZombieNautilusVariant>(
    "minecraft:zombie_nautilus_variant",
    "zombie_nautilus_variant.json",
    ZombieNautilusVariant.serializer()
)
