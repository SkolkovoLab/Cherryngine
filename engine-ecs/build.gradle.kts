plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))
    api(project(":engine-minecraft"))
    api("io.github.quillraven.fleks:Fleks:2.12")
}
