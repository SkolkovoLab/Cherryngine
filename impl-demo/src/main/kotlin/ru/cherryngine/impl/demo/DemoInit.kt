package ru.cherryngine.impl.demo

import com.github.quillraven.fleks.configureWorld
import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.StableTicker
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
import ru.cherryngine.impl.demo.ecs.testimpl.systems.*
import ru.cherryngine.impl.demo.via.MicronautViaBackwardsConfig
import ru.cherryngine.impl.demo.via.MicronautViaVersionConfig
import ru.cherryngine.impl.demo.world.TestWorldShit
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.via.initViaVersion
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class DemoInit(
    minecraftServer: MinecraftServer,
    testWorldShit: TestWorldShit,
    viaVersionConfig: MicronautViaVersionConfig,
    viaBackwardsConfig: MicronautViaBackwardsConfig,
) {
    init {
        val demoPacketHandler = DemoPacketHandler("normal")

        val fleksWorld = configureWorld {
            systems {
                add(PlayerInitSystem(demoPacketHandler))
                add(ReadClientPositionSystem(demoPacketHandler))
                add(CommandSystem())
                add(AxolotlModelSystem(demoPacketHandler))
                add(WorldSystem(testWorldShit))
                add(ViewSystem(demoPacketHandler))
                add(WriteClientPositionSystem(demoPacketHandler))

                add(ClearEventsSystem())
            }
        }


        fleksWorld.entity {
            it += ViewableComponent("normal")
            it += WorldComponent("normal")
        }

        fleksWorld.entity {
            it += ViewableComponent("winter")
            it += WorldComponent("winter")
        }

        val tickDuration = 50.milliseconds
        val ticker = StableTicker(tickDuration) { _, _ ->
            fleksWorld.update(tickDuration)
        }
        ticker.start()
        minecraftServer.start(demoPacketHandler)

        initViaVersion(minecraftServer, viaVersionConfig, viaBackwardsConfig)
    }
}