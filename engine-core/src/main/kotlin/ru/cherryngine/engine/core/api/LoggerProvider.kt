package ru.cherryngine.engine.core.api

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Prototype
import io.micronaut.inject.InjectionPoint
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Factory
class LoggerProvider {
    // Unsatisfied dependency: no bean matches the injection point
    @Prototype
    fun logger(injectionPoint: InjectionPoint<*>): Logger {
        return LoggerFactory.getLogger(injectionPoint.declaringBean.declaringType.get())
    }
}