plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":lib-jackson"))
    api(project(":lib-math"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.jdk8)
    api(libs.guava)

    implementation("io.github.dockyardmc:dockyard:0.9.3")

    api(libs.hollowcube.polar)


    implementation("org.jctools:jctools-core:4.0.3")
}