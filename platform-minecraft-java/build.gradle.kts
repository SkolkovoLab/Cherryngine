plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))
    api(project(":lib-jackson"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.text.minimessage)
    api(libs.kyori.adventure.text.serializer.plain)

    implementation("com.github.luben:zstd-jni:1.5.5-3") // polar

    // from lib-minecraft

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
    api(libs.kyori.adventure.text.serializer.gson)
    api(libs.minestom)

    // Networking
    api(libs.netty.all)

    // Logging
    api(libs.slf4j.api)
    api(libs.logback.classic)

    // Other
    api(libs.gson)
    api(libs.fastutil)
}
