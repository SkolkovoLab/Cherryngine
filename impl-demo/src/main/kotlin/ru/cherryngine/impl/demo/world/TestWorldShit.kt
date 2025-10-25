package ru.cherryngine.impl.demo.world

import jakarta.inject.Singleton
import ru.cherryngine.lib.minecraft.registry.DimensionTypes
import ru.cherryngine.impl.demo.world.polar.PolarWorldGenerator

@Singleton
class TestWorldShit {
    val worldBytes = javaClass.getResource("/world.polar")!!.readBytes()
    val world = PolarWorldGenerator.loadWorld("world", DimensionTypes.OVERWORLD, worldBytes)
}