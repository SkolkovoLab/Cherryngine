plugins {
    id("cherryngine-micronaut-lib")
}

repositories {
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
}

dependencies {
    api(project(":platform-minecraft-java"))
    api(project(":platform-minecraft-bedrock"))
}
