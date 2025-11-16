plugins {
    id("cherryngine-micronaut-app")
}

dependencies {
    api(project(":engine-core"))

    api(project(":lib-polar"))

    api(project(":engine-integration:viaversion"))
    api(project(":engine-integration:grim"))

    api("io.github.quillraven.fleks:Fleks:2.12")
}
