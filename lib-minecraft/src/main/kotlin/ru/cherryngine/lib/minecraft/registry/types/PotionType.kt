package ru.cherryngine.lib.minecraft.registry.types

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer
import ru.cherryngine.lib.minecraft.utils.registry.StaticProtocolObject

@Serializable
data class PotionType(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
) : StaticProtocolObject