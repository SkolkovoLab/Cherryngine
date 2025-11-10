package ru.cherryngine.impl.demo.ecs.testimpl.systems

import net.kyori.adventure.text.Component
import ru.cherryngine.impl.demo.ecs.GameObject
import ru.cherryngine.impl.demo.ecs.GameScene
import ru.cherryngine.impl.demo.ecs.GameSystem
import ru.cherryngine.impl.demo.ecs.testimpl.components.AxolotlModelComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.entity.AxolotlMeta
import ru.cherryngine.lib.minecraft.registry.EntityTypes
import kotlin.random.Random

class AxolotlModelSystem(
    val gameScene: GameScene,
) : GameSystem {
    // TODO сделать чтоб не утекало
    val models = hashMapOf<GameObject, McEntity>()

    override fun tick(tickIndex: Long, tickStartMs: Long) {
        gameScene.objectsWithComponent(AxolotlModelComponent::class).forEach { (gameObject, _) ->
            val connection = gameObject.getComponent(PlayerComponent::class)?.connection
            val entity = models.computeIfAbsent(gameObject) {
                McEntity(Random.nextInt(1000, 1_000_000), EntityTypes.AXOLOTL).apply {
                    metadata[AxolotlMeta.HAS_NO_GRAVITY] = true
                    metadata[AxolotlMeta.VARIANT] = AxolotlMeta.Variant.entries.random()
                    metadata[AxolotlMeta.CUSTOM_NAME] = Component.text(connection?.address.toString())
                    metadata[AxolotlMeta.CUSTOM_NAME_VISIBLE] = true
                    viewerPredicate = { it != connection }
                }
            }

            gameObject.getComponent(ClientPositionComponent::class)?.also { posComponent ->
                entity.teleport(posComponent.clientPosition, posComponent.clientYawPitch)
            }

            val viewableProvider = ViewableProvider.Static(setOf(entity))
            gameObject.setEvent(ViewableProvidersEvent::class, ViewableProvidersEvent(setOf(viewableProvider), setOf()))
        }
    }
}