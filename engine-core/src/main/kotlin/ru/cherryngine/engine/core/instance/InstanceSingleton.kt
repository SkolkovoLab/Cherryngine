package ru.cherryngine.engine.core.instance

import jakarta.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

@MustBeDocumented
@Retention(RUNTIME)
@Scope
annotation class InstanceSingleton(
    val eagerInit: Boolean = false,
    val platform: String = "",
)
