plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":platform-minecraft-java"))
    compileOnly(project(":platform-minecraft-java:integration:viaversion"))

    api("ac.grim.grimac:common:2.3.73-c2c044f+lite-no_relocate")
    api("ac.grim.grimac:GrimAPI:1.2.0.0")
    api("com.github.retrooper:packetevents-api:2.11.1+af5bbc0-SNAPSHOT")
    api("com.github.retrooper:packetevents-netty-common:2.11.1+af5bbc0-SNAPSHOT")

    runtimeOnly("org.xerial:sqlite-jdbc:3.51.0.0")
}
