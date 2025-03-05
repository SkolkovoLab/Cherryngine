package ru.cherryngine.impl.demo

import io.micronaut.context.annotation.Parameter
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.MetadataDef
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import ru.cherryngine.engine.core.entity.EngineEntity
import ru.cherryngine.engine.core.ext.minestomPos
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.modules.client.ClientModule
import ru.cherryngine.engine.scenes.modules.physics.api.Collider
import ru.cherryngine.engine.scenes.view.Viewable
import ru.cherryngine.engine.scenes.view.Viewer

@ModulePrototype
class Projectile(
    @Parameter override val gameObject: GameObject,
    @Parameter val shooter: Shooter
) : Viewable {

    val entity = EngineEntity(EntityType.ITEM_DISPLAY).apply {
        editEntityMeta {
            it.set(MetadataDef.ItemDisplay.DISPLAYED_ITEM, ItemStack.of(Material.FIRE_CHARGE))
            it.set(
                MetadataDef.ItemDisplay.POSITION_ROTATION_INTERPOLATION_DURATION,
                1
            )
        }
    }

    var damage = 50.0

    override fun onEvent(event: Event) {
        when (event) {
            is Scene.Events.Tick.Physics -> {
                gameObject.transform.translation += gameObject.transform.rotation.asView().direction() * .5
                entity.updatePositionAndRotation(gameObject.transform.global.translation, gameObject.transform.global.rotation.asView())
                entity.editEntityMeta {
                    it.set(
                        MetadataDef.ItemDisplay.SCALE,
                        gameObject.transform.global.scale.minestomPos()
                    )
                }
                if (damage-- <= 0) gameObject.destroy()
            }
            is Collider.Events.Collide -> {
                event.getOther(this)?.let { other ->
                    if (other.gameObject != shooter.gameObject) {
                        other.gameObject.getModule(Health::class)?.let { health ->
                            health.damage(damage)
                            damage = 0.0
                        }
                    }
                }
            }
        }
    }

    override fun showFor(viewer: Viewer): Boolean {
        return when (viewer) {
            is ClientModule -> {
                entity.updatePositionAndRotation(gameObject.transform.global.translation)
                entity.show(viewer.connection)
                true
            }
            else -> viewer.show(this)
        }
    }

    override fun hideFor(viewer: Viewer): Boolean {
        return when (viewer) {
            is ClientModule -> {
                entity.hide(viewer.connection)
                true
            }
            else -> viewer.show(this)
        }
    }

}