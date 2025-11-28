package ru.cherryngine.lib.minecraft.registry.registries

import ru.cherryngine.lib.minecraft.registry.DynamicRegistry
import ru.cherryngine.lib.minecraft.registry.entries.DimensionType

object DimensionTypeRegistry : DynamicRegistry<DimensionType>(
    "minecraft:dimension_type"
) {
    init {
        addEntry(
            DimensionType(
                "minecraft:overworld",
                ambientLight = 0.0f,
                bedWorks = true,
                coordinateScale = 1.0,
                effects = "minecraft:overworld",
                hasCeiling = false,
                hasRaids = true,
                hasSkylight = true,
                height = 384,
                infiniburn = "#minecraft:infiniburn_overworld",
                logicalHeight = 384,
                minY = -64,
                monsterSpawnBlockLightLimit = 0,
                monsterSpawnLightLevel = DimensionType.MonsterSpawnLightLevel(7, 0, "minecraft:uniform"),
                natural = true,
                piglinSafe = false,
                respawnAnchorWorks = false,
                ultraWarm = false,
            )
        )
        addEntry(
            DimensionType(
                "minecraft:overworld_caves",
                ambientLight = 0.0f,
                bedWorks = true,
                coordinateScale = 1.0,
                effects = "minecraft:overworld",
                hasCeiling = true,
                hasRaids = true,
                hasSkylight = true,
                height = 384,
                infiniburn = "#minecraft:infiniburn_overworld",
                logicalHeight = 384,
                minY = -64,
                monsterSpawnBlockLightLimit = 0,
                monsterSpawnLightLevel = DimensionType.MonsterSpawnLightLevel(7, 0, "minecraft:uniform"),
                natural = true,
                piglinSafe = false,
                respawnAnchorWorks = false,
                ultraWarm = false,
            )
        )
        addEntry(
            DimensionType(
                "minecraft:the_end",
                ambientLight = 0.0f,
                bedWorks = false,
                coordinateScale = 1.0,
                effects = "minecraft:the_end",
                hasCeiling = false,
                hasRaids = true,
                hasSkylight = false,
                height = 256,
                infiniburn = "#minecraft:infiniburn_end",
                logicalHeight = 256,
                minY = 0,
                monsterSpawnBlockLightLimit = 0,
                monsterSpawnLightLevel = DimensionType.MonsterSpawnLightLevel(7, 0, "minecraft:uniform"),
                natural = false,
                piglinSafe = false,
                respawnAnchorWorks = false,
                ultraWarm = false,
                fixedTime = 6000L,
            )
        )
        addEntry(
            DimensionType(
                "minecraft:the_nether",
                ambientLight = 0.1f,
                bedWorks = false,
                coordinateScale = 8.0,
                effects = "minecraft:the_nether",
                hasCeiling = true,
                hasRaids = false,
                hasSkylight = false,
                height = 256,
                infiniburn = "#minecraft:infiniburn_nether",
                logicalHeight = 128,
                minY = 0,
                monsterSpawnBlockLightLimit = 15,
                monsterSpawnLightLevel = DimensionType.MonsterSpawnLightLevel(7, 7, "minecraft:uniform"),
                natural = false,
                piglinSafe = true,
                respawnAnchorWorks = true,
                ultraWarm = true,
                fixedTime = 18000L,
            )
        )
    }
}

