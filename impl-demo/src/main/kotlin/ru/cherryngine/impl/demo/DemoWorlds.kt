package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.engine.core.world.world.LayerWorldViewableProviderImpl
import ru.cherryngine.engine.core.world.world.WorldViewableProviderImpl
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.lib.minecraft.registry.registries.DimensionType
import ru.cherryngine.lib.minecraft.world.World
import ru.cherryngine.lib.polar.PolarWorldGenerator

@Singleton
class DemoWorlds {
    private fun loadChunks(dimensionType: DimensionType, name: String): World {
        val chunks = PolarWorldGenerator.loadChunks(
            javaClass.getResource("/${name}.polar")!!.readBytes(),
            dimensionType
        )
        return World(dimensionType, chunks)
    }

    val normalWorld = WorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "de_cache_normal"))
    val winterWorld = WorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "de_cache_winter"))
    val dustWorld = WorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "de_dust2"))
    val lobbyWorld = WorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "lobby"))

    val streetWorld = WorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "street"))
    val apart1World = LayerWorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "apart1"))
    val apart2World = LayerWorldViewableProviderImpl(loadChunks(DimensionTypes.OVERWORLD, "apart2"))

    val worlds = mapOf(
        "normal" to normalWorld,
        "winter" to winterWorld,
        "dust" to dustWorld,
        "lobby" to lobbyWorld,
        "street" to streetWorld,
        "apart1" to apart1World,
        "apart2" to apart2World,
    )
}