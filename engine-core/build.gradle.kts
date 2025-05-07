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

    implementation("org.jctools:jctools-core:4.0.3")
}