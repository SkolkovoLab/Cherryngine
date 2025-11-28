package ru.cherryngine.lib.minecraft.registry.entries

import net.kyori.adventure.nbt.CompoundBinaryTag
import ru.cherryngine.lib.minecraft.nbt.nbt
import ru.cherryngine.lib.minecraft.registry.RegistryEntry

data class DimensionType(
    val identifier: String,
    val ambientLight: Float,
    val bedWorks: Boolean,
    val coordinateScale: Double,
    val effects: String,
    val hasCeiling: Boolean,
    val hasRaids: Boolean,
    val hasSkylight: Boolean,
    val height: Int,
    val infiniburn: String,
    val logicalHeight: Int,
    val minY: Int,
    val monsterSpawnBlockLightLimit: Int,
    val monsterSpawnLightLevel: MonsterSpawnLightLevel,
    val natural: Boolean,
    val piglinSafe: Boolean,
    val respawnAnchorWorks: Boolean,
    val ultraWarm: Boolean,
    val fixedTime: Long? = null,
    val cloudHeight: Int? = null
) : RegistryEntry {
    override fun getEntryIdentifier(): String {
        return identifier
    }

    override fun getNbt(): CompoundBinaryTag {
        val nbt = nbt {
            withFloat("ambient_light", ambientLight)
            withBoolean("bed_works", bedWorks)
            withDouble("coordinate_scale", coordinateScale)
            withString("effects", effects)
            if (fixedTime != null) withLong("fixed_time", fixedTime)
            withBoolean("has_ceiling", hasCeiling)
            withBoolean("has_raids", hasRaids)
            withBoolean("has_skylight", hasSkylight)
            withInt("height", height)
            withString("infiniburn", infiniburn)
            withInt("logical_height", logicalHeight)
            withInt("min_y", minY)
            withInt("monster_spawn_block_light_limit", monsterSpawnBlockLightLimit)
            withCompound("monster_spawn_light_level", monsterSpawnLightLevel.toNBT())
            withBoolean("natural", natural)
            withBoolean("piglin_safe", piglinSafe)
            withBoolean("respawn_anchor_works", respawnAnchorWorks)
            withBoolean("ultrawarm", ultraWarm)
            cloudHeight?.let { withInt("cloud_height", cloudHeight) }
        }
        return nbt
    }

    data class MonsterSpawnLightLevel(
        val maxInclusive: Int,
        val minInclusive: Int,
        val type: String,
    ) {
        fun toNBT(): CompoundBinaryTag {
            return nbt {
                withInt("max_inclusive", maxInclusive)
                withInt("min_inclusive", minInclusive)
                withString("type", type)
            }
        }
    }
}