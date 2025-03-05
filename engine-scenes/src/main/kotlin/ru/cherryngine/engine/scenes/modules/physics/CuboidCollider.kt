package ru.cherryngine.engine.scenes.modules.physics

import io.micronaut.context.annotation.Parameter
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.modules.physics.api.Collider
import ru.cherryngine.lib.math.Cuboid

@ModulePrototype
class CuboidCollider(
    @Parameter override val gameObject: GameObject,
    @Parameter var localCuboid: Cuboid
) : Collider {

    val globalCuboid
        get() = gameObject.transform.global.let {
            Cuboid.Companion.fromTwoPoints(it.translation + localCuboid.min, it.translation + localCuboid.max)
        }

    override fun isCollide(other: Collider): Boolean {
        return when (other) {
            is CuboidCollider -> {
                globalCuboid.isCollide(other.globalCuboid) || other.globalCuboid.isCollide(globalCuboid)
            }
            else -> other.isCollide(this)
        }
    }

}