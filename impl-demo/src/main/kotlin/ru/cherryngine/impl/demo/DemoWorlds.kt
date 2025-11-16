package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.world.world.LayerWorldImpl
import ru.cherryngine.engine.core.world.world.MixedWorld
import ru.cherryngine.engine.core.world.world.WorldImpl
import ru.cherryngine.lib.minecraft.protocol.types.ChunkPos
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.lib.minecraft.world.chunk.Chunk
import ru.cherryngine.lib.polar.PolarWorldGenerator

@Singleton
class DemoWorlds {
    private fun loadChunks(name: String): Map<ChunkPos, Chunk> {
        return PolarWorldGenerator.loadChunks(
            javaClass.getResource("/${name}.polar")!!.readBytes(),
            DimensionTypes.OVERWORLD
        )
    }

    val normalWorld = WorldImpl("normal", DimensionTypes.OVERWORLD, loadChunks("de_cache_normal"))
    val winterWorld = WorldImpl("winter", DimensionTypes.OVERWORLD, loadChunks("de_cache_winter"))
    val basseinWorld = WorldImpl("bassein", DimensionTypes.OVERWORLD, loadChunks("bassein"))
    val normalWithBasseinWorld =
        MixedWorld("normal_bassein", DimensionTypes.OVERWORLD, listOf(normalWorld, basseinWorld))
    val winterWithBasseinWorld =
        MixedWorld("winter_bassein", DimensionTypes.OVERWORLD, listOf(winterWorld, basseinWorld))
    val dustWorld = WorldImpl("dust", DimensionTypes.OVERWORLD, loadChunks("de_dust2"))
    val lobbyWorld = WorldImpl("lobby", DimensionTypes.OVERWORLD, loadChunks("lobby"))

    val streetWorld = WorldImpl("street", DimensionTypes.OVERWORLD, loadChunks("street"))
    val apart1World = LayerWorldImpl("apart1", DimensionTypes.OVERWORLD, loadChunks("apart1"))
    val apart2World = LayerWorldImpl("apart2", DimensionTypes.OVERWORLD, loadChunks("apart2"))

    val worlds = sequenceOf(
        normalWorld,
        winterWorld,
        basseinWorld,
        normalWithBasseinWorld,
        winterWithBasseinWorld,
        dustWorld,
        lobbyWorld,
        streetWorld,
        apart1World,
        apart2World,
    ).associateBy { it.name }
}