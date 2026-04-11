plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))
    api(project(":lib-world"))
    api(project(":lib-minecraft"))
    api(libs.mcprotocollib)
    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
}
