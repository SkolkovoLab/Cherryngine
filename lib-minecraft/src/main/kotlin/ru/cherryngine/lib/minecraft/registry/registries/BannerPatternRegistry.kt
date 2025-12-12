package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.KtJsonDataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.entries.BannerPattern

object BannerPatternRegistry : KtJsonDataDrivenRegistry<BannerPattern>(
    "minecraft:banner_pattern",
    "banner_pattern.json",
    BannerPattern.serializer()
)

