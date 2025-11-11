plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

repositories {
    mavenCentral()
    google() // Compose-Multiplatform
    maven("https://jitpack.io/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net") // Brigadier
    maven("https://repo.cherry.pizza/repository/maven-public/")
    maven("https://repo.viaversion.com/")
}
