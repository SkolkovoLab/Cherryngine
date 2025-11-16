plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-minecraft"))
    api(project(":lib-jackson"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    api(libs.kyori.adventure.api)
    api(libs.kyori.adventure.text.minimessage)
    api(libs.kyori.adventure.text.serializer.plain)

    api(libs.cloud.core)
    api(libs.cloud.annotations)
    api(libs.cloud.kotlin.coroutines)
    api(libs.cloud.kotlin.coroutines.annotations)
    api(libs.cloud.kotlin.extensions)

    implementation(libs.slf4j.bridge.jul)
}