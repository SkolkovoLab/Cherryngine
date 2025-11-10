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
    }

    fun createGameObject(): GameObject {
        val gameObject = GameObject(this)
        gameObjects.add(gameObject)
        return gameObject
    }

    fun objectsWithComponent(key: KClass<out GameComponent>): List<GameObject> {
        return gameObjects.filter { it.components.keys.contains(key) }
    }
}