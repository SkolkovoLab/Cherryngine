package ru.cherryngine.impl.demo

import com.github.quillraven.fleks.configureWorld
import jakarta.inject.Singleton
import ru.cherryngine.impl.demo.ecs.StableTicker
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
import ru.cherryngine.impl.demo.ecs.testimpl.systems.*
import ru.cherryngine.impl.demo.via.MicronautViaBackwardsConfig
import ru.cherryngine.impl.demo.via.MicronautViaVersionConfig
import ru.cherryngine.lib.minecraft.MinecraftServer
import ru.cherryngine.lib.via.initViaVersion
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class DemoInit(
    minecraftServer: MinecraftServer,
    demoWorlds: DemoWorlds,
    viaVersionConfig: MicronautViaVersionConfig,
    viaBackwardsConfig: MicronautViaBackwardsConfig,
) {
    init {
        val demoPacketHandler = DemoPacketHandler("street")

        val fleksWorld = configureWorld {
            systems {
                add(PlayerInitSystem(demoPacketHandler))
                add(ReadClientPositionSystem(demoPacketHandler))
                add(CommandSystem())
                add(AxolotlModelSystem(demoPacketHandler))
                add(WorldSystem(demoWorlds))
                add(ApartSystem())
                add(ViewSystem(demoPacketHandler))
                add(WriteClientPositionSystem(demoPacketHandler))

                add(ClearEventsSystem())
            }
        }

        fleksWorld.entity {
            it += ViewableComponent(setOf("street"))
            it += WorldComponent("street")
        }

        fleksWorld.entity {
            it += ViewableComponent(setOf("apart1"))
            it += WorldComponent("apart1")
        }

        fleksWorld.entity {
            it += ViewableComponent(setOf("apart2"))
            it += WorldComponent("apart2")
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