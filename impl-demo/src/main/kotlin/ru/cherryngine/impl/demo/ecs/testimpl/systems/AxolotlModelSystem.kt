package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import net.kyori.adventure.text.Component
import ru.cherryngine.impl.demo.ecs.eventsComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.AxolotlModelComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.ClientPositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.entity.AxolotlMeta
import ru.cherryngine.lib.minecraft.registry.EntityTypes
import kotlin.random.Random

class AxolotlModelSystem() : IteratingSystem(
    family { all(AxolotlModelComponent) }
) {
    // TODO сделать чтоб не утекало
    val models = hashMapOf<Entity, McEntity>()

    override fun onTickEntity(entity: Entity) {
        val connection = entity.getOrNull(PlayerComponent)?.connection
        val mcEntity = models.computeIfAbsent(entity) {
            McEntity(Random.nextInt(1000, 1_000_000), EntityTypes.AXOLOTL).apply {
                metadata[AxolotlMeta.HAS_NO_GRAVITY] = true
                metadata[AxolotlMeta.VARIANT] = AxolotlMeta.Variant.entries.random()
                metadata[AxolotlMeta.CUSTOM_NAME] = Component.text(connection?.address.toString())
                metadata[AxolotlMeta.CUSTOM_NAME_VISIBLE] = true
                viewerPredicate = { it != connection }
            }
        }

        entity.getOrNull(ClientPositionComponent)?.also { posComponent ->
            mcEntity.teleport(posComponent.clientPosition, posComponent.clientYawPitch)
        }

        val viewableProvider = ViewableProvider.Static(setOf(mcEntity))
        entity.eventsComponent()[ViewableProvidersEvent::class] = ViewableProvidersEvent(setOf(viewableProvider), setOf())
    }
}