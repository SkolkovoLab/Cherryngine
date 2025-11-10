package ru.cherryngine.impl.demo.ecs

import kotlin.reflect.KClass

class GameObject(
    val scene: GameScene,
) {
    val components = hashMapOf<KClass<out GameComponent>, GameComponent>()
    val events = hashMapOf<KClass<out GameEvent>, GameEvent>()

    fun <T : GameComponent> setComponent(key: KClass<out T>, component: T) {
        components[key] = component
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : GameComponent> getComponent(key: KClass<T>): T? {
        return components[key] as T?
    }

    fun <T : GameEvent> setEvent(key: KClass<out T>, component: T) {
        events[key] = component
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : GameEvent> getEvent(key: KClass<T>): T? {
        return events[key] as T?
    }
}