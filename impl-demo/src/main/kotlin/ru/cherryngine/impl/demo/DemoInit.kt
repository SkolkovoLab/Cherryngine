package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.testimpl.systems.PacketLogGameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.PlayerInitGameSystem
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
        val playerInitGameSystem = PlayerInitGameSystem(scene, testWorldShit)
        scene.gameSystems.add(playerInitGameSystem)
//        scene.gameSystems.add(PacketLogGameSystem(scene))
        scene.gameSystems.add(WorldSystem(scene))
        scene.start()
        dockyardServer.start(playerInitGameSystem)
    }
}