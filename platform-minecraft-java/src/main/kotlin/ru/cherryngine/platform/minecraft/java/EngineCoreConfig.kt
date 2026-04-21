package ru.cherryngine.platform.minecraft.java

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("engine.minecraft")
data class EngineCoreConfig(
    val address: String = "0.0.0.0",
    val port: Int = 25565,
    val mojangAuth: Boolean = false,
    val compressionThreshold: Int = 256,
)