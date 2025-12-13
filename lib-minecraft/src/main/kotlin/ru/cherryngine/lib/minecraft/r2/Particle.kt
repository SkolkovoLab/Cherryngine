package ru.cherryngine.lib.minecraft.r2

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

@Serializable
data class Particle(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
    val hasData: Boolean,
) : StaticProtocolObject