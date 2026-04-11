package ru.cherryngine.engine.mcprotocollib

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("engine.mcprotocollib")
data class McProtocolLibConfig(
    val host: String = "0.0.0.0",
    val port: Int = 25566,
)
