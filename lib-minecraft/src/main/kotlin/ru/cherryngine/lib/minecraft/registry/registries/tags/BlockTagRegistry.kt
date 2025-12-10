package ru.cherryngine.lib.minecraft.registry.registries.tags

import ru.cherryngine.lib.minecraft.registry.TagRegistry
import ru.cherryngine.lib.minecraft.registry.registries.BlockRegistry

object BlockTagRegistry : TagRegistry(
    "minecraft:block",
    "tags/block.json",
    BlockRegistry
)