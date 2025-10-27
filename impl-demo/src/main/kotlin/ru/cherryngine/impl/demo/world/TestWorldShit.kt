package ru.cherryngine.impl.demo.world

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.world.polar.PolarWorldGenerator
import ru.cherryngine.impl.demo.world.world.MixedWorld
import ru.cherryngine.impl.demo.world.world.WorldImpl
import ru.cherryngine.lib.minecraft.registry.DimensionTypes

@Singleton
class TestWorldShit {
    val normalChunks = PolarWorldGenerator.loadChunks(
        javaClass.getResource("/de_cache_normal.polar")!!.readBytes()
    )
    val winterChunks = PolarWorldGenerator.loadChunks(
        javaClass.getResource("/de_cache_winter.polar")!!.readBytes()
    )
    val basseinChunks = PolarWorldGenerator.loadChunks(
        javaClass.getResource("/bassein.polar")!!.readBytes()
    )

    val normalWorld = WorldImpl("normal", DimensionTypes.OVERWORLD, normalChunks)
    val winterWorld = WorldImpl("winter", DimensionTypes.OVERWORLD, winterChunks)
    val basseinWorld = WorldImpl("bassein", DimensionTypes.OVERWORLD, basseinChunks)
    val normalWithBasseinWorld = MixedWorld("normal_bassein", DimensionTypes.OVERWORLD, listOf(normalWorld, basseinWorld), basseinWorld)
    val winterWithBasseinWorld = MixedWorld("winter_bassein", DimensionTypes.OVERWORLD, listOf(winterWorld, basseinWorld), basseinWorld)

    val worlds = mapOf(
        "normal" to normalWorld,
        "winter" to winterWorld,
        "bassein" to basseinWorld,
        "normal_bassein" to normalWithBasseinWorld,
        "winter_bassein" to winterWithBasseinWorld
    )
}