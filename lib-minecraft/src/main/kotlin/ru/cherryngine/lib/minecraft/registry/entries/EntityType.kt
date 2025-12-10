package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

@Serializable
data class EntityType(
    val identifier: String,
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
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag? = null
}