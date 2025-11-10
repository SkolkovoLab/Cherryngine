package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameObject
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.PacketsEvent
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundChatCommandPacket

class CommandSystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithEvent(PacketsEvent::class).forEach { (gameObject, playerComponent) ->
            playerComponent.packets.forEach { packet ->
                if (packet is ServerboundChatCommandPacket) {
                    onCommand(gameObject, packet.command)
                }
            }
        }
    }

    fun onCommand(gameObject: GameObject, command: String) {
        val playerComponent = gameObject.getComponent(PlayerComponent::class) ?: return
        val split = command.split(" ")

        when (split.getOrNull(0)) {
            "world" -> {
                val viewContextID = split[1]
                gameObject.setComponent(PlayerComponent::class, playerComponent.copy(viewContextID = viewContextID))
                val viewableComponent = gameObject.getComponent(ViewableComponent::class)
                if (viewableComponent != null) {
                    gameObject.setComponent(
                        ViewableComponent::class,
                        viewableComponent.copy(viewContextID = viewContextID)
                    )
                }
            }
        }
    }
}