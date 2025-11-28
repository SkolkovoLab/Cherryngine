package ru.cherryngine.lib.minecraft.registry.entries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.Vec3DListSerializer
import ru.cherryngine.lib.minecraft.utils.kotlinx.Vec3DSerializer

@Serializable
data class EntityType(
    val identifier: String,
    val displayName: String,
    val category: String,
    val despawnDistance: Int,
    val isFriendly: Boolean,
    val isPersistent: Boolean,
    val maxInstancesPerChunk: Int,
    val noDespawnDistance: Int,
    val immuneToFire: Boolean,
    val immuneBlocks: List<String>,
    val dimensions: EntityDimensions,
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag? = null

    @Serializable
    data class EntityDimensions(
        val eyeHeight: Float,
        val fixed: Boolean,
        val height: Float,
        val width: Float,
        @Serializable(with = Vec3DSerializer::class)
        val nameTagLocation: Vec3D?,
        @Serializable(with = Vec3DListSerializer::class)
        val passengerLocations: List<Vec3D>?,
        @Serializable(with = Vec3DSerializer::class)
        val vehicleLocation: Vec3D?,
        @Serializable(with = Vec3DSerializer::class)
        val wardenChestLocation: Vec3D?,
    )
}