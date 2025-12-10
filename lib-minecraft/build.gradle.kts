plugins {
    id("cherryngine-kotlin")
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(project(":lib-math"))

    // Kotlin
    api(libs.kotlin.reflect)
    api(libs.kotlinx.serialization.core)
    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)

    // Minecraft
    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.nbt)
    api(project(":lib-adventure-serializer-nbt"))
    api(libs.kyori.adventure.text.serializer.gson)
    implementation("net.minestom:data:1.21.8-rv1")

    // Networking
    api(libs.netty.all)

    // Logging
    api(libs.slf4j.api)
    api(libs.logback.classic)

    // Other
    api(libs.gson)
    api(libs.fastutil)
}