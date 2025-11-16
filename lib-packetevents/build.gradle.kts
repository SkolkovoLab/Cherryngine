plugins {
    id("cherryngine-kotlin")
}

dependencies {
    api(project(":lib-minecraft"))

    api("com.github.retrooper:packetevents-api:2.10.2+8c92928-SNAPSHOT")
    api("com.github.retrooper:packetevents-netty-common:2.10.2+8c92928-SNAPSHOT")
}