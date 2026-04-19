plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

repositories {
    mavenCentral()
    google() // Compose-Multiplatform
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://central.sonatype.com/repository/maven-snapshots/")
    maven("https://libraries.minecraft.net") // Brigadier
    maven("https://repo.cherry.pizza/repository/maven-public/")
    maven("https://repo.viaversion.com/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://nexus.scarsz.me/content/groups/public/") // Configuralize for GrimAC
    maven("https://repo.opencollab.dev/maven-releases/") // MCProtocolLib deps
    maven("https://repo.opencollab.dev/maven-snapshots/") // MCProtocolLib
}
