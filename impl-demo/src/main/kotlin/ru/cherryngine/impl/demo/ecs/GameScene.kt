package ru.cherryngine.impl.demo.ecs

import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.milliseconds

class GameScene {
    val ticker = StableTicker(50.milliseconds, ::tick)
    val gameObjects = ArrayList<GameObject>()
    val gameSystems = ArrayList<GameSystem>()

    fun start() {
        ticker.start()
    }

    fun tick(tickIndex: Long, tickStartMs: Long) {
        gameSystems.forEach { it.tick(tickIndex, tickStartMs) }
        gameObjects.forEach { it.events.clear() }
    }

    fun createGameObject(): GameObject {
        val gameObject = GameObject(this)
        gameObjects.add(gameObject)
        return gameObject
    }

    fun <T : GameComponent> objectsWithComponent(key: KClass<T>): List<Pair<GameObject, T>> {
        return gameObjects.mapNotNull {
            val component = it.getComponent(key) ?: return@mapNotNull null
            it to component
        }
    }

    fun <T : GameEvent> objectsWithEvent(key: KClass<T>): List<Pair<GameObject, T>> {
        return gameObjects.mapNotNull {
            val event = it.getEvent(key) ?: return@mapNotNull null
            it to event
        }
    }
}