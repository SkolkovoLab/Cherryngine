package ru.cherryngine.engine.scenes.modules.physics.api

import ru.cherryngine.engine.scenes.GameObject
import ru.cherryngine.engine.scenes.api.Module
import ru.cherryngine.engine.scenes.event.Event

interface Collider : Module {

    fun onCollide(other: Collider) {
        scene.fireEvent(Events.Collide(this, other))
    }

    fun isCollide(other: Collider): Boolean

    interface Events {
        class Collide (
            val first: Collider, val second: Collider
        ) : Event {

            fun getOther(module: Module): Collider? {
                return getOther(module.gameObject)
            }

            fun getOther(gameObject: GameObject): Collider? {
                return when {
                    first.gameObject == gameObject -> second
                    second.gameObject == gameObject -> first
                    else -> null
                }
            }

        }
    }
}