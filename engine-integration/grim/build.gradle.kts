plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":engine-core"))
    compileOnly(project(":lib-viaversion"))

    api("ac.grim.grimac:common:2.3.73-9b500a2+lite-no_relocate")
    api("ac.grim.grimac:GrimAPI:1.2.0.0")
    api("com.github.retrooper:packetevents-api:2.10.2+8c92928-SNAPSHOT")
    api("com.github.retrooper:packetevents-netty-common:2.10.2+8c92928-SNAPSHOT")

    runtimeOnly("org.xerial:sqlite-jdbc:3.51.0.0")
}
