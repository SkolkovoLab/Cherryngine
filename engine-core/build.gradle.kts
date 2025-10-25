plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-minecraft"))
    api(project(":lib-jackson"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    implementation("com.github.luben:zstd-jni:1.5.5-3")

    implementation("org.jctools:jctools-core:4.0.3")
}