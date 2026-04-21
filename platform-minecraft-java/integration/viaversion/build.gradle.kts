plugins {
    id("cherryngine-micronaut-lib")
}

dependencies {
    api(project(":platform-minecraft-java"))

    api(libs.viaversion.common) {
        exclude(group = "org.slf4j", module = "org.slf4j")
    }

    api(libs.viabackwards.common) {
        exclude(group = "org.slf4j", module = "org.slf4j")
    }
}
