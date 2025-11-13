package ru.cherryngine.impl.demo.ecs.testimpl.systems

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import net.kyori.adventure.text.Component
import ru.cherryngine.impl.demo.DemoPacketHandler
import ru.cherryngine.impl.demo.ecs.testimpl.components.AxolotlModelComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PlayerComponent
import ru.cherryngine.impl.demo.ecs.testimpl.components.PositionComponent
import ru.cherryngine.impl.demo.ecs.testimpl.events.ViewableProvidersEvent
import ru.cherryngine.impl.demo.entity.McEntity
import ru.cherryngine.impl.demo.view.ViewableProvider
import ru.cherryngine.lib.minecraft.entity.AxolotlMeta
import ru.cherryngine.lib.minecraft.registry.EntityTypes
import kotlin.random.Random

class AxolotlModelSystem(
    val demoPacketHandler: DemoPacketHandler,
) : IteratingSystem(
    family { all(AxolotlModelComponent) }
) {
    private val models = HashMap<Entity, McEntity>()

    override fun onTick() {
        models.keys.removeIf { !world.contains(it) }
        super.onTick()
    }

    override fun onTickEntity(entity: Entity) {
        val playerComponent = entity.getOrNull(PlayerComponent)
        val name = playerComponent?.uuid?.let { demoPacketHandler.players[it] }?.connection?.gameProfile?.username
        val mcEntity = models.computeIfAbsent(entity) {
            McEntity(Random.nextInt(1000, 1_000_000), EntityTypes.AXOLOTL).apply {
                metadata[AxolotlMeta.HAS_NO_GRAVITY] = true
                metadata[AxolotlMeta.VARIANT] = AxolotlMeta.Variant.entries.random()
                if (name != null) metadata[AxolotlMeta.CUSTOM_NAME] = Component.text(name)
                metadata[AxolotlMeta.CUSTOM_NAME_VISIBLE] = true
                if (playerComponent != null) {
                    viewerPredicate = { it != demoPacketHandler.players[playerComponent.uuid] }
                }
            }
        }

        entity.getOrNull(PositionComponent)?.also { posComponent ->
            mcEntity.teleport(posComponent.position, posComponent.yawPitch)
        }

        val viewableProvider = ViewableProvider.Static(setOf(mcEntity))
        entity.configure {
            val event = it.getOrAdd(ViewableProvidersEvent, ::ViewableProvidersEvent)
            event.viewableProviders += viewableProvider
        }
    }
}