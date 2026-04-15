package ru.cherryngine.engine.bedrock

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("engine.bedrock")
data class BedrockConfig(
    val host: String = "0.0.0.0",
    val port: Int = 19132,
)
