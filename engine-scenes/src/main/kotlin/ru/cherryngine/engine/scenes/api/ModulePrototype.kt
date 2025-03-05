@file:Suppress("unused")

package ru.cherryngine.engine.scenes.api

import io.micronaut.context.annotation.Prototype
import kotlin.reflect.KClass

@Prototype
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ModulePrototype(
    val tickAfter: Array<KClass<out Module>> = [],
    val tickBefore: Array<KClass<out Module>> = [],
    val tickPriority: Priority = Priority.NORMAL,
) {
    enum class Priority {
        FIRST,
        NORMAL,
        LAST,
    }
}