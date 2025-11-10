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
        val demoPacketHandler = DemoPacketHandler("normal")

        val scene = GameScene {
            systems {
                add(PlayerInitSystem(demoPacketHandler))
                add(CommandSystem())
                add(ClientPositionSystem())
                add(AxolotlModelSystem())
                add(WorldSystem(testWorldShit))
                add(ViewSystem())

                add(ClearEventsSystem())
            }
        }
        val fleksWorld = scene.fleksWorld

        fleksWorld.entity {
            it += ViewableComponent("normal")
            it += WorldComponent("normal")
        }

        fleksWorld.entity {
            it += ViewableComponent("winter")
            it += WorldComponent("winter")
        }

        scene.start()
        dockyardServer.start(demoPacketHandler)
    }
}