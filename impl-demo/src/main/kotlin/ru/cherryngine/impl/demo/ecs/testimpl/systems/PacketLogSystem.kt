package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent

class PacketLogSystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithEvent(PacketsEvent::class).forEach { (_, packetsEvent) ->
            println(packetsEvent.packets)
        }
    }
}