plugins {
    id("cherryngine-kotlin")
}

dependencies {
    api(project(":lib-minecraft"))

    api("com.viaversion:viaversion-common:5.5.1") {
        exclude(group = "org.slf4j", module = "org.slf4j")
    }
    api("com.viaversion:viabackwards-common:5.5.1") {
        exclude(group = "org.slf4j", module = "org.slf4j")
    }
}