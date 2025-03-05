package ru.cherryngine.impl.demo

import io.micronaut.context.annotation.Parameter
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.MetadataDef
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import ru.cherryngine.engine.core.entity.EngineEntity
import ru.cherryngine.engine.core.ext.minestomVec
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.Scene
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.event.Event
import ru.cherryngine.engine.scenes.models.Models
import ru.cherryngine.engine.scenes.models.builder.free
import ru.cherryngine.engine.scenes.modules.ModelRenderer
import ru.cherryngine.lib.math.Vec3D

@ModulePrototype
class PlayerModelRenderer(
    @Parameter override val gameObject: GameObject
) : ModelRenderer(gameObject) {
    override val model: Models = free {
        entity("head", EngineEntity(EntityType.ITEM_DISPLAY).apply {
            editEntityMeta {
                it.set(MetadataDef.ItemDisplay.DISPLAYED_ITEM, ItemStack.of(Material.OBSERVER))
            }
        })
        entity("body", EngineEntity(EntityType.ITEM_DISPLAY).apply {
            editEntityMeta {
                it.set(MetadataDef.ItemDisplay.DISPLAYED_ITEM, ItemStack.of(Material.MELON))
            }
        })
    }

    val head get() = model.getPart("head")?.first()
    val body get() = model.getPart("body")?.first()

    override fun whenShow() {
        onUpdate()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Scene.Events.Tick.Physics -> {
                onUpdate()
            }
        }
    }

    fun onUpdate() {
        gameObject.transform.global.apply {
            val bodyScale = scale.times(1.0, .75, 1.0)
            body?.updatePositionAndRotation(translation + bodyScale.times(.0, .5, .0))
            body?.editEntityMeta {
                it.set(
                    MetadataDef.ItemDisplay.SCALE,
                    bodyScale.minestomVec()
                )
            }

            val headScale = Vec3D(1.0, 1.0, 1.0)
            head?.updatePositionAndRotation(translation + bodyScale.times(.0, 1.0, .0) + headScale.times(.0, .5, .0) - Vec3D(0.0, .1, .0))
            head?.editEntityMeta {
                it.set(
                    MetadataDef.ItemDisplay.SCALE,
                    headScale.minestomVec()
                )
                val view = rotation
                it.set(
                    MetadataDef.ItemDisplay.ROTATION_LEFT,
                    floatArrayOf(view.x.toFloat(), view.y.toFloat(), view.z.toFloat(), view.w.toFloat())
                )
            }
        }
    }
}