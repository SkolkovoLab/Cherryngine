package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.BannerPattern

object BannerPatternRegistry : DataDrivenRegistry<BannerPattern>(
    "minecraft:banner_pattern",
    "registry/banner_pattern_registry.json.gz",
    BannerPattern.serializer()
)

