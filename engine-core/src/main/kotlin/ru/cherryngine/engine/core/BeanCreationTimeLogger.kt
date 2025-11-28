package ru.cherryngine.engine.core

import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.context.event.StartupEvent
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class BeanCreationTimeLogger : BeanCreatedEventListener<Any>, ApplicationEventListener<StartupEvent> {
    private var destroyed = false
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val creationTimeMap = mutableMapOf<String, Long>()
    private var prevCreatedAt = System.currentTimeMillis()

    override fun onCreated(event: BeanCreatedEvent<Any>): Any {
        val bean = event.getBean()
        if (destroyed || !event.beanDefinition.isSingleton) return bean
        val now = System.currentTimeMillis()
        val creationTime = now - prevCreatedAt
        prevCreatedAt = now
        creationTimeMap[bean.javaClass.name] = creationTime
        return bean
    }

    override fun onApplicationEvent(event: StartupEvent) {
        destroyed = true
        creationTimeMap.asSequence()
            .filter { it.value > 100 } // выводим только те которые больше 100мс создавались
            .sortedBy { it.value }
            .forEach { (bean, time) ->
                logger.info("Bean $bean initialized in $time ms")
            }
        creationTimeMap.clear()
        event.source.destroyBean(this)
    }
}

