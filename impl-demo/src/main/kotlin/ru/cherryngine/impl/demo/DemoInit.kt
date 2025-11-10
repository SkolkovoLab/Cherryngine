package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.systems.*
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.lib.minecraft.DockyardServer

@Singleton
class DemoInit(
    dockyardServer: DockyardServer,
    testWorldShit: TestWorldShit,
) {
    init {
        val scene = GameScene()

        scene.createGameObject()[ViewableComponent::class] = ViewableComponent(
            "normal",
            setOf(),
            setOf(testWorldShit.normalWorld)
        )

        scene.createGameObject()[ViewableComponent::class] = ViewableComponent(
            "winter",
            setOf(),
            setOf(testWorldShit.winterWorld)
        )

        val playerInitSystem = PlayerInitSystem(scene, "normal")
        scene.gameSystems.add(playerInitSystem)
        scene.gameSystems.add(CommandSystem(scene))
        scene.gameSystems.add(ClientPositionSystem(scene))
        scene.gameSystems.add(EntitySystem(scene))
        scene.gameSystems.add(ViewSystem(scene))
        scene.start()
        dockyardServer.start(playerInitSystem)
    }
}