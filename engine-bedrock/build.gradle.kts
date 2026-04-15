plugins {
    id("cherryngine-micronaut-lib")
}

repositories {
    maven("https://repo.opencollab.dev/maven-releases/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
}

dependencies {
    api(project(":engine-core"))
    api("org.cloudburstmc.protocol:bedrock-connection:3.0.0.Beta12-SNAPSHOT")
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
}
