package ru.cherryngine.lib.kotlinx

import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlin.reflect.KClass

internal val KClass<*>.firstGenericType get() = this.supertypes.first().arguments.first().type!!.classifier as KClass<*>

fun Iterable<KSerializer<*>>.module() = SerializersModule {
    for (processor in this@module) {
        val targetClass = processor::class.firstGenericType
        @Suppress("UNCHECKED_CAST")
        contextual(targetClass as KClass<Any>, processor as KSerializer<Any>)
    }
}