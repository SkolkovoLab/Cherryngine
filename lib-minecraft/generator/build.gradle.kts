plugins {
    id("cherryngine-kotlin")
    application
    alias(libs.plugins.kotlin.serialization)
}

application {
    mainClass = "ru.cherryngine.lib.minecraft.generator.Main"
}

dependencies {
    // Kotlin
    api(libs.kotlin.reflect)
    api(libs.kotlinx.serialization.core)
    api(libs.kotlinx.serialization.json)

    api("com.squareup:kotlinpoet:2.2.0")

    implementation("net.minestom:data:1.21.8-rv1")
}

tasks {
    named<JavaExec>("run") {
        workingDir = projectDir.resolve("run/").apply { mkdirs() }
        standardInput = System.`in`
    }
}