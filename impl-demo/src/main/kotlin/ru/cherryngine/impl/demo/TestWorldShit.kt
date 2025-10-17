package ru.cherryngine.impl.demo

import io.github.dockyardmc.cherry.polar.PolarWorldGenerator
import io.github.dockyardmc.registry.DimensionTypes
import jakarta.inject.Singleton

@Singleton
class TestWorldShit {
    val worldBytes = javaClass.getResource("/world.polar")!!.readBytes()
    val world = PolarWorldGenerator.loadWorld("world", DimensionTypes.OVERWORLD, worldBytes)
}