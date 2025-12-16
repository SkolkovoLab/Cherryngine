package ru.cherryngine.lib.minecraft.registry.types

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer
import ru.cherryngine.lib.minecraft.utils.registry.StaticProtocolObject

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