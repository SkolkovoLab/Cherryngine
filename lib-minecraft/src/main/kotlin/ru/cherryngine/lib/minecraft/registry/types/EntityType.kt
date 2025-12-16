package ru.cherryngine.lib.minecraft.registry.types

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key
import ru.cherryngine.lib.minecraft.utils.kotlinx.KeySerializer
import ru.cherryngine.lib.minecraft.utils.registry.StaticProtocolObject

@Serializable
data class EntityType(
    @Serializable(KeySerializer::class)
    override val key: Key,
    override val id: Int,
    val translationKey: String,
    val packetType: String,
    val width: Float,
    val height: Float,
    val eyeHeight: Float,
    val attachments: Map<String, List<FloatArray>> = mapOf(),
    val drag: Float = 0.02f,
    val acceleration: Float = 0.08f,
    val fireImmune: Boolean = false,
    val clientTrackingRange: Int,
    val defaultAttributes: Map<String, Double> = mapOf(),
) : StaticProtocolObject