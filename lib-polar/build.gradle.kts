plugins {
    id("cherryngine-kotlin")
}

dependencies {
    api(project(":lib-minecraft"))
    api(project(":lib-world"))

    implementation("com.github.luben:zstd-jni:1.5.5-3")
}