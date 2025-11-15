package ru.cherryngine.impl.demo

import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.configureWorld
import jakarta.inject.Singleton
import ru.cherryngine.engine.core.PlayerManager
import ru.cherryngine.impl.demo.ecs.StableTicker
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.WorldComponent
import ru.cherryngine.impl.demo.ecs.testimpl.systems.*
import ru.cherryngine.lib.minecraft.MinecraftServer
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class DemoInit(
    minecraftServer: MinecraftServer,
    demoWorlds: DemoWorlds,
    playerManager: PlayerManager,
) {
    val fleksWorld: World

    init {
        fleksWorld = configureWorld {
            systems {
                // чтение сосотяния клиента
                add(PlayerInitSystem("street", playerManager))
                add(ReadClientPositionSystem(playerManager))

                // всякие действия
                add(CommandActionsSystem())
                add(AxolotlModelSystem(playerManager))
                add(WorldSystem(demoWorlds))
                add(ApartSystem())

                // завершение
                add(ViewSystem(playerManager))
                add(WriteClientPositionSystem(playerManager))
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
        minecraftServer.start(playerManager)
    }
}