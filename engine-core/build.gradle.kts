plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-minecraft"))
    api(project(":lib-jackson"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    api(libs.cloud.core)
    api(libs.cloud.annotations)
    api(libs.cloud.kotlin.coroutines)
    api(libs.cloud.kotlin.coroutines.annotations)
    api(libs.cloud.kotlin.extensions)

    implementation("com.github.luben:zstd-jni:1.5.5-3")
}