plugins {
    id("cherryngine-micronaut-lib")
    kotlin("plugin.serialization") version "2.1.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api(project(":lib-math"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")

    implementation("com.charleskorn.kaml:kaml:0.72.0")

    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.text.minimessage)

    api(libs.typetools)

    api(libs.minestom)
}
