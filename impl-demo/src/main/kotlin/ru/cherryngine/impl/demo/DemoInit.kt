package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.systems.ClientPositionSystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.EntitySystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.PlayerInitSystem
import ru.cherryngine.impl.demo.ecs.testimpl.systems.ViewSystem
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.lib.minecraft.DockyardServer
import java.util.*

@Singleton
class DemoInit(
    dockyardServer: DockyardServer,
    testWorldShit: TestWorldShit,
) {
    init {
        val viewContextUUID = UUID.randomUUID()
        val scene = GameScene()
        val worldGameObject = scene.createGameObject()
        worldGameObject[ViewableComponent::class] = ViewableComponent(
            viewContextUUID,
            setOf(),
            setOf(testWorldShit.normalWorld)
        )

        val playerInitSystem = PlayerInitSystem(scene, viewContextUUID)
        scene.gameSystems.add(playerInitSystem)
        scene.gameSystems.add(ClientPositionSystem(scene))
        scene.gameSystems.add(EntitySystem(scene))
        scene.gameSystems.add(ViewSystem(scene))
        scene.start()
        dockyardServer.start(playerInitSystem)
    }
}