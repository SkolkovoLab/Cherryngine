package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.ChatType

object ChatTypeRegistry : DynamicRegistry<ChatType>(
    "minecraft:chat_type"
)

