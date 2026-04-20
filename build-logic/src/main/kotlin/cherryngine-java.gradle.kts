plugins {
    id("java-library")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
}

// Kotlin 2.2 can only target bytecode up to JVM 24, but Minestom requires JVM 25 at runtime.
// Override the consumer's org.gradle.jvm.version so Gradle accepts JVM-25 artifacts;
// we run on JDK 25 (see toolchain above), so 24-bytecode code executes fine alongside 25-bytecode deps.
configurations.configureEach {
    if (isCanBeResolved) {
        attributes {
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 25)
        }
    }
}

repositories {
    mavenCentral()
    google() // Compose-Multiplatform
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://libraries.minecraft.net") // Brigadier
    maven("https://repo.cherry.pizza/repository/maven-public/")
    maven("https://repo.viaversion.com/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://nexus.scarsz.me/content/groups/public/") // Configuralize for GrimAC
    maven("https://repo.opencollab.dev/maven-releases/") // MCProtocolLib deps
    maven("https://repo.opencollab.dev/maven-snapshots/") // MCProtocolLib
}
