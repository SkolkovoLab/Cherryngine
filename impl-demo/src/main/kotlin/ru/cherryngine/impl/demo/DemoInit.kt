package ru.cherryngine.impl.demo

import io.github.dockyardmc.DockyardServer
import io.github.dockyardmc.events.Events
import io.github.dockyardmc.events.PlayerJoinEvent
import io.github.dockyardmc.events.PlayerSpawnEvent
import io.github.dockyardmc.location.Location
import io.github.dockyardmc.player.systems.GameMode
import io.github.dockyardmc.registry.DimensionTypes
import io.github.dockyardmc.world.WorldManager
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.polar.PolarWorldGenerator

@Singleton
class DemoInit(
    @Suppress("unused") dockyardServer: DockyardServer,
) {
    init {
        val worldBytes = javaClass.getResource("/world.polar")!!.readBytes()
        val worldGenerator = PolarWorldGenerator(worldBytes) // PolarWorldGenerator(worldBytes)
        val customWorld = WorldManager.create("custom_world", worldGenerator, DimensionTypes.OVERWORLD)
        customWorld.defaultSpawnLocation = Location(100, 100, 100, customWorld)

        Events.on<PlayerSpawnEvent> {
            it.world = customWorld
        }
        Events.on<PlayerJoinEvent> {
            it.player.gameMode.value = GameMode.SPECTATOR
        }
    }
}