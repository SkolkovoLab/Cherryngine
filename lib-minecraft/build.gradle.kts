plugins {
    id("cherryngine-kotlin")
    kotlin("plugin.serialization") version "2.1.0"
}

dependencies {
    api(project(":lib-math"))

    // Kotlin
    api("org.jetbrains.kotlin:kotlin-reflect:2.1.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    api("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    // Minecraft
    api("net.kyori:adventure-api:4.25.0-nbt")
    api("net.kyori:adventure-nbt:4.25.0-nbt")
    api("net.kyori:adventure-text-serializer-nbt:4.25.0-nbt")
    api("net.kyori:adventure-text-serializer-gson:4.25.0-nbt")

    // Networking
    api("io.netty:netty-all:4.2.6.Final")

    // Logging
    api("org.slf4j:slf4j-api:2.0.9")
    api("ch.qos.logback:logback-classic:1.4.14")

    // Other
    api("com.google.code.gson:gson:2.10.1")
    api("it.unimi.dsi:fastutil:8.5.13")
}