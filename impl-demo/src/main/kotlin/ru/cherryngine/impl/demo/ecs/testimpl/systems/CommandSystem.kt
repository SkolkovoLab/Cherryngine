package ru.cherryngine.impl.demo.ecs.testimpl.systems

import ru.cherryngine.impl.demo.ecs.GameObject
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ViewableComponent
import ru.cherryngine.lib.minecraft.protocol.packets.play.serverbound.ServerboundChatCommandPacket

class CommandSystem(
    val gameScene: GameScene,
) : GameSystem {
    override fun tick(tickIndex: Long, tickStartMs: Long) {
        val gameObjects = gameScene.objectsWithComponent(PlayerComponent::class)
        gameObjects.forEach { gameObject ->
            val playerComponent = gameObject[PlayerComponent::class]!!
            playerComponent.packets.forEach { packet ->
                if (packet is ServerboundChatCommandPacket) {
                    onCommand(gameObject, playerComponent, packet.command)
                }
            }
        }
    }

    fun onCommand(gameObject: GameObject, playerComponent: PlayerComponent, command: String) {
        val split = command.split(" ")

        when (split.getOrNull(0)) {
            "world" -> {
                val viewContextID = split[1]
                gameObject[PlayerComponent::class] = playerComponent.copy(viewContextID = viewContextID)
                val viewableComponent = gameObject[ViewableComponent::class]
                if (viewableComponent != null) {
                    gameObject[ViewableComponent::class] = viewableComponent.copy(viewContextID = viewContextID)
                }
            }
        }
    }
}