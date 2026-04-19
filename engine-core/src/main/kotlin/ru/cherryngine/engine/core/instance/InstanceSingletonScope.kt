package ru.cherryngine.engine.core.instance

import io.micronaut.context.scope.BeanCreationContext
import io.micronaut.context.scope.CustomScope
import io.micronaut.inject.BeanIdentifier
import jakarta.inject.Singleton
import java.util.*

@Singleton
class InstanceSingletonScope : CustomScope<InstanceSingleton> {

    companion object {
        private val current = ThreadLocal<Instance>()

        fun currentInstance(): Instance? = current.get()

        fun <T> withInstance(instance: Instance, block: () -> T): T {
            val prev = current.get()
            current.set(instance)
            return try {
                block()
            } finally {
                current.set(prev)
            }
        }
    }

    override fun annotationType(): Class<InstanceSingleton> = InstanceSingleton::class.java

    override fun <T : Any> getOrCreate(creationContext: BeanCreationContext<T>): T {
        val instance = current.get()
            ?: error(
                "@InstanceSingleton '${creationContext.definition().beanType.name}' " +
                    "requested outside of an Instance"
            )

        return instance.getOrCreate(creationContext.definition().beanType) {
            creationContext.create().bean()
        }
    }

    override fun <T : Any> remove(identifier: BeanIdentifier): Optional<T> = Optional.empty()
}
