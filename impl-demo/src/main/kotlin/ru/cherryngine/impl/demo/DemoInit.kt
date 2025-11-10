package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.testimpl.systems.ClientPositionSystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.PlayerInitSystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.ViewSystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.WorldSystem
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.lib.minecraft.DockyardServer

@Singleton
class DemoInit(
    dockyardServer: DockyardServer,
    testWorldShit: TestWorldShit,
) {
    init {
        val scene = GameScene()
        val playerInitSystem = PlayerInitSystem(scene, testWorldShit)
        scene.gameSystems.add(playerInitSystem)
        scene.gameSystems.add(ClientPositionSystem(scene))
//        scene.gameSystems.add(PacketLogGameSystem(scene))
        scene.gameSystems.add(WorldSystem(scene))
        scene.gameSystems.add(ViewSystem(scene))
        scene.start()
        dockyardServer.start(playerInitSystem)
    }
}