package ru.cherryngine.engine.core

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("engine.core")
data class EngineCoreConfig(
    val address: String = "0.0.0.0",
    val port: Int = 25565,
    val mojangAuth: Boolean = false,
    val compressionThreshold: Int = 256,
)