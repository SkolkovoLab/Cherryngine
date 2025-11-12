plugins {
    id("cherryngine-kotlin")
}

dependencies {
    api(project(":lib-minecraft"))

    api("ac.grim.grimac:common:2.3.72")
    api("ac.grim.grimac:GrimAPI:1.1.0.0")
    api("com.github.retrooper:packetevents-api:2.10.1")
}