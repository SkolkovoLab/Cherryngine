plugins {
    id("cherryngine-kotlin")
}

dependencies {
    api(project(":lib-minecraft"))

    api(libs.viaversion.common) {
        exclude(group = "org.slf4j", module = "org.slf4j")
    }

    api(libs.viabackwards.common) {
        exclude(group = "org.slf4j", module = "org.slf4j")
    }
}