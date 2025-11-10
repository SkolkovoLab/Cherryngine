package ru.cherryngine.impl.demo

import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
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

        scene.createGameObject().apply {
            setComponent(ViewableComponent::class, ViewableComponent("normal"))
            setComponent(WorldComponent::class, WorldComponent("normal"))
        }
        scene.createGameObject().apply {
            setComponent(ViewableComponent::class, ViewableComponent("winter"))
            setComponent(WorldComponent::class, WorldComponent("winter"))
        }

        val playerInitSystem = PlayerInitSystem(scene, "normal")
        scene.gameSystems.add(playerInitSystem)
        scene.gameSystems.add(CommandSystem(scene))
        scene.gameSystems.add(ClientPositionSystem(scene))
        scene.gameSystems.add(AxolotlModelSystem(scene))
        scene.gameSystems.add(WorldSystem(scene, testWorldShit))
        scene.gameSystems.add(ViewSystem(scene))
        scene.start()
        dockyardServer.start(playerInitSystem)
    }
}