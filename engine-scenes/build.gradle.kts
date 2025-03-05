plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))

    // jgrapht
    api(libs.jgrapht.core)
    api(libs.jgrapht.io)
}
