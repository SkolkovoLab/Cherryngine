plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-jackson"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    api(project(":lib-protocol"))

    implementation("com.github.luben:zstd-jni:1.5.5-3")

    implementation("org.jctools:jctools-core:4.0.3")
}