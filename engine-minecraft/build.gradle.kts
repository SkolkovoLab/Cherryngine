plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))
    api(project(":lib-minecraft"))
    api(project(":lib-jackson"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.text.minimessage)
    api(libs.kyori.adventure.text.serializer.plain)
}
