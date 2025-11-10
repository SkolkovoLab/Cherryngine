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
    val dustChunks = PolarWorldGenerator.loadChunks(
        javaClass.getResource("/de_dust2.polar")!!.readBytes()
    )
    val lobbyChunks = PolarWorldGenerator.loadChunks(
        javaClass.getResource("/lobby.polar")!!.readBytes()
    )

    val normalWorld = WorldImpl("normal", DimensionTypes.OVERWORLD, normalChunks)
    val winterWorld = WorldImpl("winter", DimensionTypes.OVERWORLD, winterChunks)
    val basseinWorld = WorldImpl("bassein", DimensionTypes.OVERWORLD, basseinChunks)
    val normalWithBasseinWorld =
        MixedWorld("normal_bassein", DimensionTypes.OVERWORLD, listOf(normalWorld, basseinWorld))
    val winterWithBasseinWorld =
        MixedWorld("winter_bassein", DimensionTypes.OVERWORLD, listOf(winterWorld, basseinWorld))
    val dustWorld = WorldImpl("dust", DimensionTypes.OVERWORLD, dustChunks)
    val lobbyWorld = WorldImpl("lobby", DimensionTypes.OVERWORLD, lobbyChunks)

    val worlds = sequenceOf(
        normalWorld,
        winterWorld,
        basseinWorld,
        normalWithBasseinWorld,
        winterWithBasseinWorld,
        dustWorld,
        lobbyWorld
    ).associateBy { it.name }
}