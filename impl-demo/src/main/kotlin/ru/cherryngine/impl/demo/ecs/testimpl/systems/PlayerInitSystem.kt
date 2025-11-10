package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import ru.cherryngine.impl.demo.DemoPacketHandler
import ru.cherryngine.impl.demo.ecs.eventsComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.AxolotlModelComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent

class PlayerInitSystem(
    val demoPacketHandler: DemoPacketHandler,
) : IteratingSystem(
    family { all(PlayerComponent) }
) {
    override fun onTick() {
        demoPacketHandler.toCreateEntities.forEach { connection ->
            world.entity {
                it += PlayerComponent(
                    connection,
                    demoPacketHandler.defaultViewContextID
                )

                it += ViewableComponent(demoPacketHandler.defaultViewContextID)

                it += AxolotlModelComponent
            }
        }
        demoPacketHandler.toCreateEntities.clear()

        world.family { all(PlayerComponent) }.forEach {
            val playerComponent = it[PlayerComponent]
            if (playerComponent.connection in demoPacketHandler.toRemoveEntities) {
                it.remove()
            }
        }
        demoPacketHandler.toRemoveEntities.clear()

        super.onTick()
    }

    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity[PlayerComponent]
        val connection = playerComponent.connection
        val packets = demoPacketHandler.queues.remove(connection) ?: return

        entity.eventsComponent()[PacketsEvent::class] = PacketsEvent(packets)
    }
}