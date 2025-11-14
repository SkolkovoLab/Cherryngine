plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))
    api(project(":lib-packetevents"))

    api("ac.grim.grimac:common:2.3.72")
    api("ac.grim.grimac:GrimAPI:1.1.0.0")
}
