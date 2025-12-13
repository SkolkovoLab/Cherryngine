package ru.cherryngine.lib.minecraft.r2

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

@Serializable
data class PotionType(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
) : StaticProtocolObject