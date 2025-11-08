package ru.cherryngine.lib.minecraft.registry.registries

import kotlinx.serialization.Serializable
import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.math.Vec3D
import ru.cherryngine.lib.minecraft.codec.RegistryStreamCodec
import ru.cherryngine.lib.minecraft.registry.DataDrivenRegistry
import ru.cherryngine.lib.minecraft.registry.RegistryEntry
import ru.cherryngine.lib.minecraft.utils.kotlinx.Vec3DListToJsonSerializer
import ru.cherryngine.lib.minecraft.utils.kotlinx.Vec3DToJsonSerializer

class EntityTypeRegistry : DataDrivenRegistry<EntityType>() {
    override val identifier: String = "minecraft:entity_type"
}

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
}

@Serializable
data class EntityDimensions(
    val eyeHeight: Float,
    val fixed: Boolean,
    val height: Float,
    val width: Float,
    @Serializable(with = Vec3DToJsonSerializer::class)
    val nameTagLocation: Vec3D?,
    @Serializable(with = Vec3DListToJsonSerializer::class)
    val passengerLocations: List<Vec3D>?,
    @Serializable(with = Vec3DToJsonSerializer::class)
    val vehicleLocation: Vec3D?,
    @Serializable(with = Vec3DToJsonSerializer::class)
    val wardenChestLocation: Vec3D?
)