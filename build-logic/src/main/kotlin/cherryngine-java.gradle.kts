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
    maven("https://repo.unnamed.team/repository/unnamed-public/")
    maven("https://repo.panda-lang.org/releases/") // LiteCommands
    maven("https://libraries.minecraft.net") // Brigadier
    maven("https://mvn.devos.one/releases") // DockyardMC

    maven("https://oss.sonatype.org/content/repositories/snapshots/") // spark me.lucko:bytesocks-java-client
    maven("https://repo.spliterash.ru/group/") // spark
    maven("https://repo.cherry.pizza/repository/maven-public/") // для adventure-text-serializer-nbt
    maven("https://repo.hypera.dev/snapshots/") // LuckPerms
    maven("https://repo.plasmoverse.com/releases/") // PlasmoVoice Releases
    maven("https://repo.plasmoverse.com/snapshots/") // PlasmoVoice Snapshots
    maven("https://repo.plo.su/") // PlasmoVoice Opus
    maven("https://maven.enginehub.org/repo/") // WorldEdit
    maven("https://maven.noxcrew.com/public")
    maven("https://repo.mineinabyss.com/releases") // Geary
}

configurations.all {
    exclude(group = "com.github.Minestom", module = "Minestom")
    exclude(group = "dev.hollowcube", module = "minestom-ce")
}
