package ru.cherryngine.impl.demo.ecs

import kotlin.reflect.KClass

class GameObject(
    val scene: GameScene,
) {
    val components = hashMapOf<KClass<out GameComponent>, GameComponent>()

    operator fun <T : GameComponent> set(key: KClass<out T>, component: T) {
        components[key] = component
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : GameComponent> get(key: KClass<T>): T? {
        return components[key] as T?
    }
}