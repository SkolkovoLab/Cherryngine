package ru.cherryngine.engine.scenes.modules.physics

import io.micronaut.context.annotation.Parameter
import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.ModulePrototype
import ru.cherryngine.engine.scenes.modules.physics.api.Collider
import ru.cherryngine.engine.scenes.modules.physics.api.Physics

@ModulePrototype(tickPriority = ModulePrototype.Priority.LAST)
class SimplePhysics(
    @Parameter override val gameObject: GameObject
) : Physics {

    override fun updateColliders() {
        groupElements(scene.getModules(Collider::class)) { (firstCollider, secondCollider) ->

            if (firstCollider.isCollide(secondCollider)) {
                firstCollider.onCollide(secondCollider)
                secondCollider.onCollide(firstCollider)
            }

        }
    }

    fun <T> groupElements(elements: List<T>, action: (Pair<T, T>) -> Unit) {
        elements.forEachIndexed { i, firstElement ->
            for (j in i + 1 until elements.size) {
                val secondElement = elements[j]
                val pair = Pair(firstElement, secondElement)
                action(pair)
            }
        }
    }

}