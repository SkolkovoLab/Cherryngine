package ru.cherryngine.lib.minecraft.r2

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer

@Serializable
data class Attribute(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
    val translationKey: String,
    val defaultValue: Double,
    val clientSync: Boolean,
    val maxValue: Double,
    val minValue: Double,
) : StaticProtocolObject