plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-jackson"))
    api(project(":lib-math"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    api("io.github.dockyardmc:Dockyard")

    implementation("com.github.luben:zstd-jni:1.5.5-3")

    implementation("org.jctools:jctools-core:4.0.3")

    // ECS
    val gearyVersion = "0.28.0"
    api("com.mineinabyss:geary-core:${gearyVersion}")
//    implementation("com.mineinabyss:geary-<addon-name>:${gearyVersion}")
}