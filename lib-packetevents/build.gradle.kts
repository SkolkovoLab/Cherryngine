plugins {
    id("cherryngine-kotlin")
}

dependencies {
    api(project(":lib-minecraft"))

    api("com.github.retrooper:packetevents-api:2.9.6+4519ccba2-SNAPSHOT")
    api("com.github.retrooper:packetevents-netty-common:2.9.6+4519ccba2-SNAPSHOT")
}